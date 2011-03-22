all:
	javac */*.java

GUI/SharixGUI.class: all

run: GUI/SharixGUI.class
	java GUI/SharixGUI

clean:
	rm -f */*.class
