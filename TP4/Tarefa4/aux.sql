select s.SUP_NAME as SNAME, COUNT(c.COF_NAME) as CNAME
    from COFFEES as c, SUPPLIERS as s 
    where s.SUP_ID = c.SUP_ID and s.SUP_NAME like '%Superior Coffee%'
    group by s.SUP_NAME;