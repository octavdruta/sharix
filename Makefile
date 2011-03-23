all:
	javac */*.java

Mediator/SharixMediatorMock.class: all

run: Mediator/SharixMediatorMock.class
	java Mediator/SharixMediatorMock

clean:
	rm -f */*.class
