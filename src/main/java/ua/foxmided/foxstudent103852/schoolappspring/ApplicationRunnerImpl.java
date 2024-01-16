package ua.foxmided.foxstudent103852.schoolappspring;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import ua.foxmided.foxstudent103852.schoolappspring.service.RootService;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        RootService rootService = context.getBean(RootService.class);
        rootService.main();
    }
}
