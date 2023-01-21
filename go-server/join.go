package main

import (
	"encoding/json"

	"github.com/patrickmn/go-cache"
)

func join(msg RequestMessage) string {
	game, err := findGame(msg.GameID)
	if err != nil {
		return err.Error()
	}

	player := Player{ID: msg.PlayerID, Ready: msg.Ready, conn: msg.conn}
	game.Players = append(game.Players, player)
	gameCache.Set(msg.GameID, game, cache.DefaultExpiration)

	m, err := json.Marshal(ResponseMessage{Request: msg, Game: game})
	if err != nil {
		return err.Error()
	}

	notify(game, *msg.conn, m)
	return string(m)
}
