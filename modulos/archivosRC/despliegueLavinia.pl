:- use_module(library(lists)).


/*


despliegue del texto de entrada con marcas de estructuras encontradas


*/


despliegueLavinia(Marcas,pos(In,Fin)):-
                                       writeTexto(Marcas,In,Fin).

writeTexto(Marcas,N,F) :-
                         texto(N1,N,T),
                         arco(N1,N,[_, Lema|_]),
                         finMarca(Marcas,N,MarN),  % MarN es la lista [I1-Par(Et1,[Atr1,Atr2,...]), I2-Par(Et2,[Atr21,...]),...]
                         keysort(MarN,MarNS),
                         reverse(MarNS,MarR),
                         finMarcaL(MarR),
                         writeBlank(Lema),
                         inMarca(Marcas,N,MarN1),
                         keysort(MarN1,MarN1S),
                         reverse(MarN1S,MarR1),
                         inMarcaL(MarR1,N),
                         N1 is N+1,
                         N < F,
                         writeCat(N1,N,Lema,T),
                         separadores(Lema),
                         !,
                         writeTexto(Marcas,N1,F).

writeTexto(_,_,_,_).

writeCat(_,_,_,T):-
        write(T).

writeBlank(Lema):-
        member(Lema,[';',':',')',',']),
        !.

writeBlank(_):-
        write(' ').


separadores(Lema) :-
                    es_sepa(Lema),!,nl.
separadores(_).

es_sepa('.').
es_sepa('?').
es_sepa(';').
es_sepa('...').




% tomo todos los atributos
finMarca([M|Ms],N,MarN) :-
                        findall(I-par(M,Atribs),arco(N,I,[M|Atribs]),L),
                        finMarca(Ms,N,S),
                        append(L,S,MarN).
finMarca([],_,[]).

finMarcaL([_-par(M,_)|Z]) :-
        /*  write('</'),
          write(M),
          write('>'),*/
          salto(M),
          finMarcaL(Z).
finMarcaL([]).


% tomo todos los atributos
inMarca([M|Ms],N,MarN) :-
          findall(F-par(M,Atribs),arco(F,N,[M|Atribs]),L),
          inMarca(Ms,N,S),
          append(L,S,MarN).
inMarca([],_,[]).


inMarcaL([Ind-par(M,As)|Z],N) :-
        write('<'),
        write(M),
        write(' '),
        Ind2 is Ind-1,
        write(Ind2),
        write(' '),
        write(N),
        write(' '),
        printAtributos(As,1),
        write('>'),
        inMarcaL(Z,N).

inMarcaL([],_).

printAtributos([],_).
printAtributos([Atr|As],N):-
                            write('atributo'),
                            write(N),
                            write('='),
                            write(Atr),
                            write(' '),
                            M is N+1,
                            printAtributos(As,M).

salto(enonce) :- !,nl,nl.
salto(_).
