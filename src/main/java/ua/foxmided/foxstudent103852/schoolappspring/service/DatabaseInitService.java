package ua.foxmided.foxstudent103852.schoolappspring.service;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxmided.foxstudent103852.schoolappspring.util.Constants;

@Service
public class DatabaseInitService {
    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean clearDatabase() {
        try {
            log.info("Deleting data in database");
            List<String> tablesToTruncate = new ArrayList<>();
            tablesToTruncate.add(Constants.TABLE_STUDENT_COURSE);
            tablesToTruncate.add(Constants.TABLE_STUDENTS);
            tablesToTruncate.add(Constants.TABLE_COURSES);
            tablesToTruncate.add(Constants.TABLE_GROUPS);
            for (String tableName : tablesToTruncate) {
                entityManager.createNativeQuery(String.format(Constants.SQL_TRANCATE_TABLE_CASCADE, tableName))
                        .executeUpdate();
            }
            log.info("All data in the database was successfully deleted");
            return true;
        } catch (PersistenceException e) {
            log.error(Constants.LOG_PATTERN_2_EXCEPTION,
                    "Failed to delete all data in database", e.getMessage(), e);
            return false;
        }
    }

    public boolean isDatabaseFilled() {
        log.info("Checking the filling of the database");
        Set<String> tables = new HashSet<>();
        tables.add(Constants.TABLE_COURSES);
        tables.add(Constants.TABLE_GROUPS);
        tables.add(Constants.TABLE_STUDENTS);
        try {
            for (String tableName : tables) {
                BigInteger result = (BigInteger) entityManager
                        .createNativeQuery(String.format(Constants.SQL_SELECT_COUNT, tableName))
                        .getSingleResult();
                if (result.longValue() <= Constants.DBINIT_FILLED_VALUE) {
                    log.info(Constants.LOG_PATTERN_MESSAGE_3, "Table '", tableName, "' is empty");
                    return false;
                }
            }
            log.info("Database is filled");
            return true;
        } catch (PersistenceException e) {
            log.error(Constants.LOG_PATTERN_2_EXCEPTION,
                    "Failed to check the filling of the database", e.getMessage(), e);
            return false;
        }
    }
}
