package main

import (
	"encoding/json"

	"github.com/patrickmn/go-cache"
)

func open(msg RequestMessage) string {
	game, err := findGame(msg.GameID)
	if err != nil {
		game = createGameWithId(msg.GameID)
	}

	game.conns = append(game.conns, msg.conn)
	gameCache.Set(msg.GameID, game, cache.DefaultExpiration)

	m, err := json.Marshal(ResponseMessage{Request: msg, Game: game})
	if err != nil {
		return err.Error()
	}

	return string(m)
}
