goesTo(kingWilliamsTown, amabele).
goesTo(kingWilliamsTown, eastLondon).
goesTo(kingWilliamsTown, peddie).
goesTo(bedford, adelaide).
goesTo(fortBeaufort, alice).
goesTo(tarkastad, adelaide).
goesTo(cathcart, cofimvaba).
goesTo(cathcart, whittlesea).
goesTo(somerset, cookhouse).
goesTo(alice, dimbaze).
goesTo(alice, hogsback).
goesTo(adelaide, fortBeaufort).
goesTo(dimbaze, kingWilliamsTown).

canReach(X, Y) :- goesTo(X, Y).
canReach(X, Y) :- goesTo(X, Z), canReach(Z, Y).
