package net.avdw.spyfall.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.avdw.spyfall.network.packet.RegisterUserRequest;

public class RegisterPlayer extends Listener {
    @Override
    public void received(final Connection connection, final Object object) {
        if (object instanceof RegisterUserRequest) {
            RegisterUserRequest request = (RegisterUserRequest) object;

        }
    }

}
