package fr.phunder.flashminigame.utils.count;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class CountdownTimer extends CountTimer {

    public CountdownTimer(int countdownLength, Consumer<String> consumerDuring, Runnable thenRunnable) {
        super(countdownLength, consumerDuring, thenRunnable);
    }

    @Override
    public void run() {
        if (getSecondsRemaining() < 0) {
            getThenRunnable().run();
            Bukkit.getScheduler().cancelTask(this.getTaskId());
            return;
        }
        getConsumerDuring().accept(String.valueOf(getSecondsRemaining()));
        setSecondsRemaining(getSecondsRemaining() - 1);
    }
}
