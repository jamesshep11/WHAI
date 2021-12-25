wizard(lili).
wizard(ruth).
wizard(albert).
wizard(X) :- father(Y, X), wizard(Y), mother(Z, X), wizard(Z).

father(albert, james).
father(james, harry).
mother(ruth, james).
mother(lili, harry).

scares(hagrid, dudley).

magical(X) :- wizard(X).

hates(vernon, X) :- magical(X).
hates(petunia, X) :- magical(X) ; scares(X, dudley).