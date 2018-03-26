Create Table Employee(
SSN INTEGER,
Fname CHAR(18),
Minit CHAR(18),
Lname CHAR(18),
Bdate DATETIME DAY TO DAY,
Address CHAR(18),
Sex CHAR(18),
Salary FLOAT,
SuperSsn INTEGER,
Dnumber INTEGER,
FOREIGN KEY (Dnumber) REFERENCES Department(Dnumber),PRIMARY KEY (SSN));
Create Table Department(
Dnumber INTEGER,
Dname CHAR(18),
Mgr_ssn INTEGER,
Mgr_start_date DATE,
PRIMARY KEY (Dnumber));
Create Table Dept_Loca(
Dnumber INTEGER,
Dlocation CHAR(18),
PRIMARY KEY (Dnumber, Dlocation));
Create Table Project(
Pnumber INTEGER,
Pname CHAR(18),
Plocation CHAR(18),
Dnumber INTEGER,
FOREIGN KEY (Dnumber) REFERENCES Department(Dnumber),PRIMARY KEY (Pnumber));
Create Table WorksOn(
SSN INTEGER,
Pnumber INTEGER,
Hours CHAR(18),
PRIMARY KEY (SSN, Pnumber));
Create Table Dependent(
Dependent_name CHAR(18),
Sex CHAR(18),
Bdate CHAR(18),
RelationShip CHAR(18),
SSN INTEGER,
PRIMARY KEY (Dependent_name, SSN));

