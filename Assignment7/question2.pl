directlyIn(katarina, anastasia).
directlyIn(olga, katarina).
directlyIn(natasha, olga).
directlyIn(irina, natasha).

isIn(X, Y) :- directlyIn(X, Y).
isIn(X, Y) :- directlyIn(X, Z), isIn(Z, Y).