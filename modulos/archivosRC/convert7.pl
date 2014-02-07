


:- op(1200, xfx, '#').

:- op(1150, xfx, '=>').
:- op(1100, yfy, '>>').
:- op(1100, fy, '>>').
:- op(1000, yfy, '<<').
:- op(1000, yf, '<<').
:- op(900, yfy, '//').
:- op(900, fy, '//').

:- op(900, fy, '*').

cargar_reglas(ArchEntrada):-
	read_file_to_terms(ArchEntrada,Terminos,[]),
	%open(ArchSalida, write, SSal),
	parsing(Terminos,general,[],[],Parsed,ExSet),
	assert_rules(Parsed,ExSet),
	assert_posibles.
	
	


parsing([],_,Parsed,ExSet,Parsed,ExSet):-
	!.	
/*	
parsing([T|Terminos],SSal):-
	parsing_modulo([T|Terminos],TermNew,SSal),
	!,
	parsing(TermNew,SSal).

*/



parsing([modulo#NomModulo|Ter],_,ParsAc,ExAc,Parsed,ExSet):-
	!,	
	/* change_module(NomModulo, ParsAc, ParsAc2)*/
	parsing(Ter,NomModulo,ParsAc, ExAc,Parsed,ExSet).
	

parsing([exclusion#X|Ter],Modulo,ParsAc,ExAc,Parsed,ExSet):-
	!,
	parsing_exclusion(X,EX),
	parsing(Ter,Modulo,ParsAc,[EX|ExAc], Parsed, ExSet).


parsing([Regla|Ter],Modulo,ParsAc,ExAc,Parsed,ExSet):-
	!,
	% insert_rule(Regla,ParsAc,ParsAc2),
	parsing_rule(Modulo,Regla,ReglaC),
	parsing(Ter, Modulo, [ReglaC|ParsAc], ExAc, Parsed,ExSet).
	
	

/* change_module(NomModulo, Mods, [(NomModulo,[])|Mods]):-!.*/



/* insert_rule(Regla, [(Mod,Reglas)|OtrosMod],[(Mod,[ReglaC|Reglas])|OtrosMod]):-
	parsing_rule(Mod,Regla,ReglaC). 

*/


parsing_exclusion(Nom => CatsC, exclusion(Nom,CatsR)):-
	curve_to_list2(CatsC,[],CatsR).



parsing_rule(Mod,Regla, ReglaC):-
	Regla = (Nom # LadoI => LadoD),
	get_left_context(LadoD, LeftContext, RestoLD),
	get_body(RestoLD,Body,RestoB),
	get_right_context_condition(RestoB,RightContext,Condicion),
	create_rule(Mod, Nom, LadoI, LeftContext, Body, RightContext, Condicion, ReglaC).
	
	%writeln(ReglaC).
	



get_left_context(ContextoI>>Resto, ContextoI, Resto):-!.
get_left_context(>>Resto, empty, Resto):-!.
get_body(Body<<, Body, empty):-!.
get_body(Body<<Resto, Body, Resto):-!.
get_right_context_condition(empty, empty, true):-!.
get_right_context_condition(ContextoD//Condicion, ContextoD, Condicion):-!.
get_right_context_condition(//Condicion, empty, Condicion):-!.
get_right_context_condition(ContextoD, ContextoD, true):-!.





generate([], [], []).
	
	

create_rule(Mod, Nom, LadoI, LeftContext, Body, RightContext, Condicion, ReglaC):-
	curve_to_list(LeftContext,[],Ls),
	curve_to_list(Body,[],Bs),
	curve_to_list(RightContext,[],Rs),
	append(Bs,[inicio|Ls], BsLs),
	append(Rs,[fin|BsLs], RHS),
	convert_cat(LadoI,ob(CatI)),
	right_corner(RHS,RightCorner),	

%	armar_regla(RightCorner,Mod,Nom,CatI, lDer(RHS,[]), Condicion, ReglaC).
	
	ReglaC = regla(RightCorner,Mod,Nom,CatI, lDer(RHS,[]), Condicion, true).
	



right_corner([fin,ob(Cat)|_],Cat):-
	!.
right_corner([ob(Cat)|_],Cat):-
	!.


curve_to_list2((X,Y),Ac,Ys):-
	!,
	curve_to_list2(Y,[X|Ac],Ys).
curve_to_list2(X,Ac,[X|Ac]).
	
	

curve_to_list(empty,[],[]):-
	!.
curve_to_list((X,Y),Ac,Ys):-
	!,
	convert_cat(X,Cat),
	curve_to_list(Y,[Cat|Ac],Ys).

curve_to_list(X,Ac,[Cat|Ac]):-
	convert_cat(X,Cat).


to_list(X,[X|_]).


%%%%%%%  se asume que la etiqueta es [PAL, LEMA, Atr1, Atr2, ...]

convert_cat(*(NameEx,N), ex(NameEx,N)):-!.
convert_cat(op(Etiqu), op(Etiqu)):- !.
convert_cat(no(Etiqu), no(Etiqu)):- !.
convert_cat(el(Etiqu), el(Etiqu)):- !.
convert_cat(Etiqu, ob(Etiqu)):- !.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5
/* NO HAY MAS REGLAS DE 5 ARGS
armar_regla(RightCorner,Mod,Nom,CatI, lDer(RHS,[]), empty, 
             regla(RightCorner,Mod,Nom,CatI, lDer(RHS,[]))) :-!.
*/
armar_regla(RightCorner,Mod,Nom,CatI, lDer(RHS,[]), Condicion, 
             regla(RightCorner,Mod,Nom,CatI, lDer(RHS,[]), Condicion, true)) :-!.

assert_rules(Parsed,ExSet):-
	%retractall(regla(_,_,_,_,_)),
	retractall(regla(_,_,_,_,_,_,_)),	% queda solo esta con dos args
	
	assert_rules2(Parsed,ExSet).
	% findall(X,(member(X,Ready),asserta(X)),_). %chancho but works!	
	

assert_posibles:-
	retractall(reglasPosibles(_,_,_)),
	%findall(Mod,regla(_,Mod,_,_,_), Mods2A),
	findall(Mod,regla(_,Mod,_,_,_,_,_), Mods2), %queda solo esta con 2 args
	%append(Mods2A,Mods2B,Mods2),
	%sort(Mods2,Mods),  en vez de ordenar, elimino repetidos 
	no_rep(Mods2,Mods),
	create_rp(Mods).

% aiala: hago este predicado para mantener el orden de módulos del archivo de reglas (11/2007)
no_rep([],[]) :-!.
no_rep([E|R],NR) :-
	member(E,R),
	!,
	no_rep(R,NR).
no_rep([E|R],[E|NR]) :-
	no_rep(R,NR).


create_rp([]).
create_rp([Mod|Mods]):-
	findall(RC-Mod, regla(RC,Mod,_,_,_,_,_),RCS), %agregué los dos args
	writeln(RCS),
	create_rp_module(RCS),
	create_rp(Mods).



create_rp_module([]).
create_rp_module([RC-Mod|Rs]):-
	findall(Regla, regla(RC,Mod,Regla,_,_,_,_),Reglas), % agregué los dos args
	writeln(Reglas),
	(reglasPosibles(RC,Mod,Reglas)-> 
		true; 
		assert((reglasPosibles(RC,Mod,Reglas):-!))
	),
	create_rp_module(Rs).
	
	

assert_rules2([],_).
assert_rules2([R|Rs],ExSet):-
	assert_one_rule(R,ExSet),
	!,
	assert_rules2(Rs,ExSet).


/* NO HAY MÁS REGLAS DE 5 ARGS
assert_one_rule(regla(A,B,C,D,lDer(Cats1,Cats2)),ExSet):-
	change_ex_rule(Cats1,ExSet,Cats11),
	change_ex_rule(Cats2,ExSet,Cats22),		%innecesario, se supone siempre []
	asserta(regla(A,B,C,D,lDer(Cats11,Cats22))).
*/

assert_one_rule(regla(A,B,C,D,lDer(Cats1,Cats2),Cond,true),ExSet):-
	change_ex_rule(Cats1,ExSet,Cats11),
	change_ex_rule(Cats2,ExSet,Cats22),		%innecesario, se supone siempre []
	asserta(regla(A,B,C,D,lDer(Cats11,Cats22),Cond,true)).

change_ex_rule([],_,[]).
change_ex_rule([ex(Name,N)|Rs],ExSets,[ex(List,N)|Rs2]):-
	member(exclusion(Name,List), ExSets),
	!,
	change_ex_rule(Rs,ExSets,Rs2).
	
change_ex_rule([X|Rs],ExSets,[X|Rs2]):-
	!,
	change_ex_rule(Rs,ExSets,Rs2).







