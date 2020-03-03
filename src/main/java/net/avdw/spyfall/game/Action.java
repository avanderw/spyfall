package net.avdw.spyfall.game;

public interface Action<Req, Res> {
    Res call(Req request);
}
