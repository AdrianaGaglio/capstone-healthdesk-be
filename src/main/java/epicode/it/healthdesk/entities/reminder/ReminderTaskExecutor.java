package epicode.it.healthdesk.entities.reminder;

import org.springframework.stereotype.Service;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class ReminderTaskExecutor {
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5); // 5 thread

    public void executeTask(Runnable task) {
        executorService.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void shutdown() {
        System.out.println("Arresto dell'ExecutorService...");
        executorService.shutdown();
    }
}
