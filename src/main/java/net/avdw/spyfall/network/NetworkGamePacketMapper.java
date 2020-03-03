package net.avdw.spyfall.network;

import com.esotericsoftware.kryonet.Connection;
import net.avdw.spyfall.game.accuseplayer.AccusePlayerNetworkRequest;
import net.avdw.spyfall.game.accuseplayer.AccusePlayerRequest;
import net.avdw.spyfall.game.accuseplayer.AccusePlayerResponse;
import net.avdw.spyfall.game.accuseplayer.AccusePlayerNetworkResponse;
import net.avdw.spyfall.game.addplayer.AddPlayerNetworkRequest;
import net.avdw.spyfall.game.addplayer.AddPlayerNetworkResponse;
import net.avdw.spyfall.game.askquestion.AskQuestionNetworkRequest;
import net.avdw.spyfall.game.askquestion.AskQuestionNetworkResponse;
import net.avdw.spyfall.game.askquestion.AskQuestionRequest;
import net.avdw.spyfall.game.askquestion.AskQuestionResponse;
import net.avdw.spyfall.game.addplayer.AddPlayerRequest;
import net.avdw.spyfall.game.addplayer.AddPlayerResponse;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerNetworkRequest;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerNetworkResponse;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerRequest;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerResponse;
import net.avdw.spyfall.game.startround.StartRoundNetworkRequest;
import net.avdw.spyfall.game.startround.StartRoundNetworkResponse;
import net.avdw.spyfall.game.startround.StartRoundRequest;
import net.avdw.spyfall.game.startround.StartRoundResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NetworkGamePacketMapper {
    NetworkGamePacketMapper INSTANCE = Mappers.getMapper(NetworkGamePacketMapper.class);

    ReadyPlayerNetworkResponse mapToNetworkReadyResponse(ReadyPlayerResponse readyPlayerResponse);

    AccusePlayerNetworkResponse mapToNetworkAccuseResponse(AccusePlayerResponse accusePlayerResponse);

    AskQuestionNetworkResponse mapToNetworkAskResponse(AskQuestionResponse askResponse);

    StartRoundNetworkResponse mapToNetworkStartRoundResponse(StartRoundResponse startRoundResponse);

    AddPlayerNetworkResponse mapToNetworkAddPlayerResponse(AddPlayerResponse addPlayerResponse);

    @Mapping(source = "connection.ID", target = "playerId")
    @Mapping(source = "addPlayerNetworkRequest.playerName", target = "playerName")
    AddPlayerRequest toGameAddPlayerRequest(Connection connection, AddPlayerNetworkRequest addPlayerNetworkRequest);

    @Mapping(source = "connection.ID", target = "playerId")
    ReadyPlayerRequest mapToGameReadyRequest(Connection connection, ReadyPlayerNetworkRequest readyPlayerNetworkRequest);

    StartRoundRequest toGameStartRoundRequest(StartRoundNetworkRequest startRoundNetworkRequest);

    AccusePlayerRequest mapToGameAccusePlayerRequest(AccusePlayerNetworkRequest accusePlayerNetworkRequest);

    AskQuestionRequest mapToGameAskQuestionRequest(AskQuestionNetworkRequest askQuestionNetworkRequest);
}
