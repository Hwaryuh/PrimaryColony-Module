package studio.semicolon.prc.core.module.validate.connect;

import org.bukkit.Chunk;

public interface ConnectionValidator {
    /**
     * 카메라 청크에서 인디케이터 청크로 연결 가능한지 검증
     *
     * @param cameraChunk 카메라(기존 모듈)가 위치한 청크
     * @param indicatorChunk 새로 설치할 모듈의 청크
     * @param indicatorYaw 새로 설치할 모듈의 yaw (정규화된 값)
     * @return 연결 가능 여부
     */
    boolean canConnect(Chunk cameraChunk, Chunk indicatorChunk, float indicatorYaw);
}