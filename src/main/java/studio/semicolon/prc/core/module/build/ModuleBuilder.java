package studio.semicolon.prc.core.module.build;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.Player;
import org.bukkit.structure.Structure;
import studio.semicolon.prc.Module;
import studio.semicolon.prc.core.module.ModuleMetadata;
import studio.semicolon.prc.core.module.validate.BuildValidationResult;
import studio.semicolon.prc.core.module.indicator.IndicatorState;
import studio.semicolon.prc.api.module.Structures;
import studio.semicolon.prc.core.module.ModuleType;
import studio.semicolon.prc.core.module.exception.StructureLoadException;

public class ModuleBuilder {
    private ModuleBuilder() { }

    public static BuildResult canBuild(Player player, IndicatorState state, BuildValidationResult validation) {
        if (validation.occupied()) return BuildResult.CHUNK_OCCUPIED;
        if (!validation.canConnect()) return BuildResult.CANNOT_CONNECT;
        if (!state.moduleType().getPlacementRequirement().test(player.getInventory())) return BuildResult.INSUFFICIENT_ITEM;

        return BuildResult.SUCCESS;
    }

    /**
     * 구조물 설치
     *
     * @param player 플레이어
     * @param state 인디케이터 상태
     * @param yCoordinate 설치 Y 좌표
     */
    public static void build(Player player, IndicatorState state, BuildValidationResult validation, double yCoordinate) {
        BuildResult result = canBuild(player, state, validation);
        if (!result.isSuccess()) return;

        ModuleType moduleType = state.moduleType();
        StructureRotation rotation = state.getRotation();

        Structure structure = loadStructure(moduleType);
        if (structure == null) throw new StructureLoadException(moduleType.getStructureKey());

        Location cornerLocation = Structures.getCornerLocation(state.chunk(), rotation, (int) yCoordinate);
        BuildAnimation.start(structure, cornerLocation, rotation);

        ModuleMetadata.saveMetadata(state);
        moduleType.getPlacementRequirement().consume(player.getInventory());
    }

    private static Structure loadStructure(ModuleType type) {
        NamespacedKey key = NamespacedKey.minecraft(type.getStructureKey());
        return Module.getInstance().getServer().getStructureManager().loadStructure(key);
    }
}