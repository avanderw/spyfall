package main

import "encoding/json"

func ready(msg RequestMessage) string {
	game, err := findGame(msg.GameID)
	if err != nil {
		return err.Error()
	}

	for i, p := range game.Players {
		if p.ID == msg.PlayerID {
			game.Players[i].Ready = true
		}
	}

	m, err := json.Marshal(ResponseMessage{Request: msg, Game: game})
	if err != nil {
		return err.Error()
	}

	notify(game, *msg.conn, m)
	return string(m)
}
