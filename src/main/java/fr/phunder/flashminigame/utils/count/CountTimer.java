package fr.phunder.flashminigame.utils.count;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class CountTimer extends BukkitRunnable {
    private int secondsRemaining;
    private final Consumer<String> consumerDuring;
    private final Runnable thenRunnable;

    public CountTimer(int countdownLength, Consumer<String> consumerDuring, Runnable thenRunnable) {
        this.secondsRemaining = countdownLength;
        this.consumerDuring = consumerDuring;
        this.thenRunnable = thenRunnable;

    }

    @Override
    public void run() {
        if (getSecondsRemaining() > 0) {
            getConsumerDuring().accept(String.valueOf(getSecondsRemaining()));
            setSecondsRemaining(getSecondsRemaining() + 1);
        } else {
            getThenRunnable().run();
            this.cancel();
        }
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }

    public void setSecondsRemaining(int secondsRemaining) {
        this.secondsRemaining = secondsRemaining;
    }

    public Consumer<String> getConsumerDuring() {
        return consumerDuring;
    }

    public Runnable getThenRunnable() {
        return thenRunnable;
    }
}
