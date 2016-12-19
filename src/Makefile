.PHONY: all clean test

all: $(wildcard *.java)
	javac *.java

clean:
	rm -f *~ *.class *.java.orig

test: all
	java -ea -Xmx6G Main

