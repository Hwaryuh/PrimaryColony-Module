package studio.semicolon.prc.api.machine;

import io.quill.paper.util.bukkit.pdc.PDCKeys;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MachineState {
    public enum State {
        IDLE, PROCESSING, COMPLETED
    }

    private State state;
    private long completionTime;
    private String processResult;

    // PDC Keys
    private static final String KEY_STATE = "machine_state";
    private static final String KEY_COMPLETION_TIME = "machine_completion_time";
    private static final String KEY_PROCESS_RESULT = "machine_process_result";

    public MachineState() {
        this.state = State.IDLE;
        this.completionTime = 0;
        this.processResult = null;
    }

    // State

    public State getState() {
        if (state == State.PROCESSING && System.currentTimeMillis() >= completionTime) {
            this.state = State.COMPLETED;
        }
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isIdle() {
        return getState() == State.IDLE;
    }

    public boolean isProcessing() {
        return getState() == State.PROCESSING;
    }

    public boolean isCompleted() {
        return getState() == State.COMPLETED;
    }

    // Processing

    public void startProcessing(int durationSeconds, String resultItemID) {
        this.state = State.PROCESSING;
        this.completionTime = System.currentTimeMillis() + (durationSeconds * 1000L);
        this.processResult = resultItemID;
    }

    public long getCompletionTime() {
        return completionTime;
    }

    public int getRemainingSeconds() {
        if (!isProcessing()) return 0;

        long remaining = completionTime - System.currentTimeMillis();
        if (remaining <= 0) return 0;

        return (int) Math.ceil(remaining / 1000.0);
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    // PDC

    public static MachineState load(PersistentDataContainer pdc) {
        if (pdc == null) return new MachineState();

        MachineState state = new MachineState();

        String stateStr = pdc.getOrDefault(PDCKeys.of(KEY_STATE), PersistentDataType.STRING, "IDLE");
        try {
            state.state = State.valueOf(stateStr);
        } catch (IllegalArgumentException e) {
            state.state = State.IDLE;
        }

        state.completionTime = pdc.getOrDefault(PDCKeys.of(KEY_COMPLETION_TIME), PersistentDataType.LONG, 0L);

        if (pdc.has(PDCKeys.of(KEY_PROCESS_RESULT), PersistentDataType.STRING)) {
            state.processResult = pdc.get(PDCKeys.of(KEY_PROCESS_RESULT), PersistentDataType.STRING);
        }

        return state;
    }

    public void save(PersistentDataContainer pdc) {
        if (pdc == null) return;

        getState();

        pdc.set(PDCKeys.of(KEY_STATE), PersistentDataType.STRING, state.name());
        pdc.set(PDCKeys.of(KEY_COMPLETION_TIME), PersistentDataType.LONG, completionTime);

        if (processResult != null) {
            pdc.set(PDCKeys.of(KEY_PROCESS_RESULT), PersistentDataType.STRING, processResult);
        } else if (pdc.has(PDCKeys.of(KEY_PROCESS_RESULT), PersistentDataType.STRING)) {
            pdc.remove(PDCKeys.of(KEY_PROCESS_RESULT));
        }
    }

    @Override
    public String toString() {
        return String.format("MachineState{state=%s, remaining=%ds, result=%s}", state, getRemainingSeconds(), processResult);
    }
}