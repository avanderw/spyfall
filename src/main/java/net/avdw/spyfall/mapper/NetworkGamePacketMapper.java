package net.avdw.spyfall.mapper;

import net.avdw.spyfall.game.packet.ReadyResponse;
import net.avdw.spyfall.network.packet.ReadyPlayerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NetworkGamePacketMapper {
    NetworkGamePacketMapper INSTANCE = Mappers.getMapper(NetworkGamePacketMapper.class);

    ReadyPlayerResponse mapToNetworkReadyResponse(ReadyResponse readyPlayerResponse);
}
