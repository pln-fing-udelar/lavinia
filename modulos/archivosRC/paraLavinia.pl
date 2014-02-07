
:- dynamic reglasPosibles/3, regla/5, regla/7.
:- [
        klectura,
        analisis,
        convert7,
        despliegueLavinia
   ].

principal:-
         read(ArchCondiciones),
         consult(ArchCondiciones),
         read(ArchReglas),
         cargar_reglas(ArchReglas),
         read(ArchTag),
         cargar(ArchTag),
		 findall(Mod,regla(_,Mod,_,_,_,_,_),Mods2),no_rep(Mods2,Mods),
         aplicModulos(Mods),
         findall(Et,regla(_,_,_,[Et|_],_,_,_),Ets2),no_rep(Ets2,Ets),
         ultimoNodo(N),
         despliegueLavinia(Ets, pos(1,N)).


print_texto(N,M):-
        texto(K,_,P),
        K=<M,
        K>N,
        write(P),
        write(' '),
        fail.
print_texto(_,_).

borra :-
      abolish(arco/3),

      abolish(texto/3),
      abolish(texto/4),
      abolish(sucesor/2),
      asserta(sucesor(0,1)),
      abolish(ultimoNodo/1),
      assert(ultimoNodo(1)).




cargar(FT) :-
        ignore(borra),
        cargarGrafo(FT).


aplicModulo(Modulo) :-
        modoAplic(Modulo,Modo),
        aplicModulo(Modulo,Modo).

aplicModulo(Modulo,normal):-!,
        ultimoNodo(N),
        analisis(Modulo,ot,pos(1,N)).

modoAplic(_,normal).


aplicModulos([M|Ms]):-
        aplicModulo(M),
        !,
        aplicModulos(Ms).
aplicModulos([]).
   

