package fr.phunder.flashminigame.utils.count;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class CountdownTimer extends CountTimer {

    public CountdownTimer(int countdownLength, Consumer<String> consumerDuring, Runnable thenRunnable) {
        super(countdownLength, consumerDuring, thenRunnable);
    }

    @Override
    public void run() {
        if (getSecondsRemaining() > 0) {
            getConsumerDuring().accept(String.valueOf(getSecondsRemaining()));
            setSecondsRemaining(getSecondsRemaining() - 1);
        } else {
            getThenRunnable().run();
            this.cancel();
        }
    }
}
