-- @outores: Maycon douglas batista dos santos - 11921BSI209 & Matheus costa monteiro - 12111BSI281
 
-- questão 8
/*
 * Acrescente, por exemplo, 100.000 depósitos de R$ 1,00 (Um Real) na conta do cliente
 * 'Germano Luiz de Paula', na agência 'Pampulha', na conta 93134. Você deve executar o
 * código abaixo em uma janela de comandos do PostgreSQL, quando o banco de dados
 * selecionado para consultas for o nosso banco IB.
 */

-- antes de rodar as incerçoes
select * from deposito d 
	where d.numero_conta = 93134;

do
$do$
begin
	for num in 1..100000 loop
		insert into deposito (numero_deposito, numero_conta, nome_agencia, nome_cliente, saldo_deposito) 
			values (num, 93134, 'Pampulha', 'Germano Luiz de Paula', 1.00);
	end loop;
end;
$do$;

-- após o uso do cod acima
select * from deposito d 
	where d.numero_conta = 93134;

-- questão 11.1
/*
* Selecione os nomes dos clientes e seus respectivos números de conta e nome de
* agência que fizeram depósitos e empréstimos ao mesmo tempo.
* Este código em SQL resolve esta consulta sem fazer uso da cláusula JOIN:
*/

	select e.nome_cliente, e.numero_conta, e.nome_agencia from emprestimo e
intersect
	select d.nome_cliente, d.numero_conta, d.nome_agencia from deposito d;

-- 11.2 Construa a consulta equivalente a este exemplo utilizando a cláusula JOIN.

select e.nome_cliente, e.numero_conta, e.nome_agencia
	from emprestimo e natural inner join deposito d
	group by e.nome_cliente, e.numero_conta, e.nome_agencia;
	
-- Construa a consulta equivalente a este exemplo utilizando a SELECT DISTINCT e sem o JOIN.

select distinct(e.nome_cliente || ' | ' || e.numero_conta || ' | ' || e.nome_agencia) as nome_conta_agencia
	from emprestimo e, deposito d
	where e.nome_cliente = d.nome_cliente
	and e.numero_conta = d.numero_conta 
	and e.nome_agencia = d.nome_agencia;

-- 13.1 cenário 2

create or replace function faixa_cliente(nome_cliente2 varchar(80)) returns character as
$body$
declare 
	soma_depositos float;
begin 
	
	-- soma todos os sepositos do cliente que foi passado e coloca na variavel soma depositos
	select sum(d.saldo_deposito) as total from deposito d where d.nome_cliente = nome_cliente2 into soma_depositos;

	if (soma_depositos > 6000) then
		return 'A';
	elsif soma_depositos > 4000 then
		return 'B';
	else
		return 'C'; -- como indicado pelo professor!
	end if;

end;
$body$ LANGUAGE plpgsql; -- ok funcionando!

-- usando o função acima como pedido!
select faixa_cliente(nome_cliente), nome_cliente from cliente;

-- 13.2
create  or replace function contas_cliente (nome_cliente2 varchar(80)) returns varchar as
$body$
declare 
	lista varchar;
	dados varchar;
begin 
	for dados in select c.nome_agencia  || '-' || c.numero_conta from conta c where c.nome_cliente = nome_cliente2 loop
		if lista is null then
			lista := dados;
		else
			lista := lista || ' | ' || dados;
		end if;
	end loop;

	return lista;
end;
$body$ LANGUAGE plpgsql; -- ok funcionando

-- usando a função acima
select nome_cliente, contas_cliente (nome_cliente), cidade_cliente from cliente;
