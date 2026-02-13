package studio.semicolon.prc.core.module.build;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.bukkit.Chunk;
import org.bukkit.block.structure.StructureRotation;
import studio.semicolon.prc.core.module.Structures;
import studio.semicolon.prc.core.module.ModuleType;

import java.util.List;

public final class OccupiedChunks {
    private final ImmutableList<Chunk> chunks;

    private OccupiedChunks(List<Chunk> chunks) {
        this.chunks = ImmutableList.copyOf(chunks);
    }

    /**
     * 인디케이터 상태로부터 점유 청크 목록 계산
     */
    public static OccupiedChunks from(Chunk baseChunk, ModuleType moduleType, StructureRotation rotation) {
        int chunkCount = moduleType.getChunkCount();

        if (chunkCount == 1) {
            return new OccupiedChunks(Lists.newArrayList(baseChunk));
        }

        List<Chunk> chunks = Lists.newArrayList(baseChunk);
        int[] offset = Structures.getOffsetFromRotation(rotation);

        for (int i = 1; i < chunkCount; i++) {
            chunks.add(baseChunk.getWorld().getChunkAt(
                    baseChunk.getX() + (offset[0] * i),
                    baseChunk.getZ() + (offset[1] * i)
            ));
        }

        return new OccupiedChunks(chunks);
    }

    /**
     * 불변 청크 목록 반환
     */
    public List<Chunk> asList() {
        return chunks;
    }

    /**
     * 첫 번째 청크 (기준 청크)
     */
    public Chunk getFirst() {
        return chunks.getFirst();
    }

    /**
     * 마지막 청크
     */
    public Chunk getLast() {
        return chunks.getLast();
    }

    /**
     * 청크 개수
     */
    public int size() {
        return chunks.size();
    }

    /**
     * 특정 청크가 포함되는지 확인
     */
    public boolean contains(Chunk chunk) {
        return chunks.contains(chunk);
    }
}