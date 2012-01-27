FICHIERS_PDF=$(shell ls *.dia 2> /dev/null | sed "s/.dia/.pdf/g"; ls *.eps 2> /dev/null | sed "s/.eps/.pdf/g";ls *.svg 2> /dev/null | sed "s/.svg/.pdf/g")

.SUFFIXES : .pdf .eps .tex .fig .dia .svg

all: ${FICHIERS_PDF}

.eps.pdf:
	epstopdf $*.eps

.fig.pdf:
	fig2dev -L pdf $*.fig $*.pdf

.dia.eps:
	dia --export=$*.eps --filter=eps-builtin $*.dia

.svg.pdf:
	inkscape -z -A $*.pdf $*.svg

clean:
	rm -f ${FICHIERS_PDF}
