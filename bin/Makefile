.PHONY: all clean test

all: $(wildcard *.java)
	javac *.java

clean:
	rm -f *~ *.class *.java.orig

main: all
	java -ea -Xmx6G Main

test: all
	java -ea -Xmx6G test