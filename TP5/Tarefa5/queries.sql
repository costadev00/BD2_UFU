-- 6.1
select c.nome_cliente as nome
	from cliente c 
	where c.nome_cliente in (select d.nome_cliente from deposito d)
	and c.nome_cliente not in (select e.nome_cliente  from emprestimo e);

-- 6.2
select c.nome_cliente as nome
	from cliente c 
	where c.nome_cliente in (select d.nome_cliente from deposito d)
	and c.nome_cliente in (select e.nome_cliente  from emprestimo e);