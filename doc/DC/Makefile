repertoire=../cls
TEX=$(shell pwd)/$(repertoire):$(shell kpsewhich --expand-var='$$TEXINPUTS')

all: pdf

pdf:	*.tex
	cd images; ls ; make; 
	TEXINPUTS=$(TEX) pdflatex *.tex
	TEXINPUTS=$(TEX) pdflatex *.tex
	rm -f *.aux *.cb *.cb2 *.log *.toc *.out



clean:
	rm -f *.pdf *.aux *.cb *.cb2 *.log *.toc *.out
