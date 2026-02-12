package studio.semicolon.prc.core.module.validate;

import studio.semicolon.prc.api.module.indicator.IndicatorRenderer;

public record BuildValidationResult(boolean occupied, boolean canConnect) {
    public IndicatorRenderer.RenderState toRenderState() {
        if (occupied) return IndicatorRenderer.RenderState.BLOCKED;
        if (!canConnect) return IndicatorRenderer.RenderState.WARNING;

        return IndicatorRenderer.RenderState.NORMAL;
    }
}