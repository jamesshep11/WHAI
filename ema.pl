parent(mpande, cetshwayo).
parent(ngwane, sobhuza).
parent(dinuzulu, solomon).
parent(mswatiII, ludvonga).
parent(senzangakhona, mpande).
parent(nyangayezizwe, goodwill).
parent(mbandzeni, ngwane).
parent(sobhuza, mswatiIII).
parent(senzangakhona, dingane).
parent(solomon, nyangayezizwe).
parent(mswatiII, mbandzeni).
parent(dinuzulu, athur).
parent(sobhuza, thumbumuzi).
parent(senzangakhona, ushaka).
parent(cetshwayo, dinuzulu).
parent(solomon, mcwayizeni).

brother(X, Y) :- parent(Z, X), parent(Z, Y).
uncle(X, Y) :- brother(Z, X), parent(Z, Y).
related(X, Y) :- brother(X, Y); uncle(X, Y); uncle(Y, X); parent(X, Y); parent(Y, X).