all: ejercicio1

ejercicio1: mkfs.java mkfs.class dump.java dump.class mkdir.java mkdir.class
	java mkfs filesys.dat 64 8
	java dump filesys.dat
	java mkdir /home
	java dump filesys.dat
	java mkdir /home/roma
	java ls /home
	java dump filesys.dat
	java mkdir /home/paris 

test: mkfs.java mkfs.class dump.java dump.class mkdir.java mkdir.class
	javac cp.java
	java mkfs filesys.dat 256 40
	java mkdir /home
	java mkdir /home/paris
	java mkdir /home/francia
	java mkdir /home/francia/texas
	echo "test no passed" | java tee /home/paris/paris.txt
	echo "test passed" | java tee /home/francia/francia.txt
	java cp /home/francia/francia.txt /home/paris/paris.txt
	java cat /home/paris/paris.txt
	
find:	find.java
	javac find.java
	java mkfs filesys.dat 256 40
	java mkdir /home
	java mkdir /home/paris
	java mkdir /home/francia
	java mkdir /home/alemania
	java mkdir /home/alemania/yael
	java mkdir /home/alemania/yael/so2
	java mkdir /home/francia/texas
	java mkdir /home/francia/texas/USA
	java mkdir /home/francia/texas/USA/veracruz
	java mkdir /home/francia/texas/USA/veracruz/baseDatos
	echo "test no passed" | java tee /home/paris/paris.txt
	echo "test passed" | java tee /home/francia/francia.txt
	echo "FCC" | java tee /home/francia/texas/USA/veracruz/baseDatos/fcc.txt
	java find /home

findv2:	findv2.java
	javac findv2.java
	java mkfs filesys.dat 256 40
	java mkdir /home
	java mkdir /home/paris
	java mkdir /home/francia
	java mkdir /home/alemania
	java mkdir /home/alemania/yael
	java mkdir /home/alemania/yael/so2
	java mkdir /home/francia/texas
	java mkdir /home/francia/texas/USA
	java mkdir /home/francia/texas/USA/veracruz
	java mkdir /home/francia/texas/USA/veracruz/baseDatos
	echo "test no passed" | java tee /home/paris/paris.txt
	echo "test passed" | java tee /home/francia/francia.txt
	echo "FCC" | java tee /home/francia/texas/USA/veracruz/baseDatos/fcc.txt
	java findv2 /

clean:
	rm filesys.dat