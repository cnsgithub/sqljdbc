#Reproducer for https://github.com/microsoft/mssql-jdbc/issues/1059

##Setup
````sql
create database test_sp
go

use test_sp
go

create proc test @id int,@str varchar(max) as select @id,@str
go

create login reader with password='password_reader',default_database=test_sp
create user reader for login reader
alter role db_datareader add member reader
go

create login executor with password='password_executor',default_database=test_sp
create user executor for login executor
alter role db_datareader add member executor
grant execute on test to executor
go
````

##Execution
Just execute the unit tests contained in ``MssqlJdbc1059``.

##Results
![](junit_fail.png)
