package main

import (
	"errors"
	"net"
	"time"

	"github.com/patrickmn/go-cache"
)

type Game struct {
	ID      string
	Players []Player
	conns   []*net.Conn
}

type Player struct {
	ID    string
	Ready bool
	conn  *net.Conn
}

var gameCache = cache.New(5*time.Minute, 10*time.Minute)

func findGame(gameID string) (Game, error) {
	game, found := gameCache.Get(gameID)
	if !found {
		return Game{}, errors.New("Game not found")
	}

	return game.(Game), nil
}
