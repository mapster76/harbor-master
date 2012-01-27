\ProvidesClass{picINSA}[2008/09/11 version 1.0]

\LoadClass[a4paper,twoside,12pt,openright]{report}

\RequirePackage[francais]{babel}
\RequirePackage[utf8]{inputenc}
\RequirePackage{amsmath,amssymb,amsfonts,amsthm}
\RequirePackage[cyr]{aeguill}
\RequirePackage[a4paper,twoside,lmargin=2cm,rmargin=2cm,tmargin=2.5cm,bmargin=3cm]{geometry}
\RequirePackage{graphicx}
\RequirePackage{verbatim}
\RequirePackage{moreverb}
\RequirePackage{lastpage}
\RequirePackage{fancyhdr}
\RequirePackage{multirow}
\RequirePackage{textcomp}
\RequirePackage{ifthen}
\RequirePackage{longtable}
\RequirePackage{fancybox}
\RequirePackage{array}
\RequirePackage{color}
\RequirePackage{colortbl}
\RequirePackage{makeidx}
\RequirePackage{float}
\RequirePackage{eurosym}
\RequirePackage{lscape}
\RequirePackage{tabularx}
\RequirePackage{enumerate}
\RequirePackage{ifpdf}
\RequirePackage[outerbars]{changebar}
\RequirePackage{layouts}
\RequirePackage{fp}

% changement de la police des titres

\renewcommand{\rmdefault}{phv}

% options de la classe

\newboolean{departASI}
\setboolean{departASI}{false}
\newboolean{departMRIE}
\setboolean{departMRIE}{false}

\DeclareOption{asi}{\setboolean{departASI}{true}}
\DeclareOption{mrie}{\setboolean{departMRIE}{true}}

\ProcessOptions
\newcommand{\departement}{\asiCourt{}}
%\RequirePackage{commun/vocabulaireCommun}

%\ifthenelse{\boolean{departASI}}{\RequirePackage{asi/vocabulaireASI} \newcommand{\departement}{\asiCourt{}}}{}
%\ifthenelse{\boolean{departMRIE}}{\RequirePackage{mrie/vocabulaireMRIE} \newcommand{\departement}{\mrieCourt{}}}{}

\fancypagestyle{pageIncluse}{
  \fancyhf{}
  \fancyfoot[CE]{
    \begin{picture}(0,0)
      \put(-510,-65){\includegraphics[width=350mm,height=25mm]{orange/images/Volutes3.pdf}}
      \put(-270,-20){\begin{minipage}{5cm}\raggedleft Page \thepage \end{minipage}}
      %\put(-115,-40){\begin{minipage}{11cm} \raggedright \scriptsize \textit{\mentionLegale{}} \end{minipage}}
      \put(146,-42){\begin{minipage}{5cm}\raggedleft \footnotesize  \end{minipage}}
    \end{picture}}%

  \fancyfoot[CO]{
    \begin{picture}(0,0)
      \put(-485,-65){\includegraphics[width=350mm,height=25mm]{images/Volutes3bis.pdf}}
      \put(125,-20){Page \thepage}
      %\put(-200,-40){\begin{minipage}{11cm} \raggedleft \scriptsize \textit{\mentionLegale{}} \end{minipage}}
      \put(-285,-45){\begin{minipage}{5cm} \footnotesize  \end{minipage}}
    \end{picture}}%
  \renewcommand{\topfraction}{0.85}
  \renewcommand{\textfraction}{0.1}
  \renewcommand{\floatpagefraction}{0.75}
}

\fancypagestyle{pageNormale}{ \fancyhf{}%
  \fancyhead[LE]{\itshape \titreDetaillePrive{} \referenceVersionPrive}
  \fancyhead[RO]{\itshape \titreDetaillePrive{} \referenceVersionPrive}
  \renewcommand{\headrulewidth}{0.2pt}

  \fancyfoot[CE]{
    \begin{picture}(0,0)
      \put(-510,-65){\includegraphics[width=350mm,height=25mm]{images/Volutes3.pdf}}
      \put(-270,-20){\begin{minipage}{5cm}\raggedleft Page \thepage \end{minipage}}
      %\put(0,-40){\begin{minipage}{11cm} \raggedright \scriptsize \textit{\mentionLegale{}} \end{minipage}}
      \put(146,-42){\begin{minipage}{5cm}\raggedleft \footnotesize  \end{minipage}}
    \end{picture}}%

  \fancyfoot[CO]{
    \begin{picture}(0,0)
      \put(-485,-65){\includegraphics[width=350mm,height=25mm]{images/Volutes3bis.pdf}}
      \put(125,-20){Page \thepage}
      %\put(-200,-40){\begin{minipage}{11cm} \raggedleft \scriptsize \textit{\mentionLegale{}} \end{minipage}}
      \put(-285,-45){\begin{minipage}{5cm} \footnotesize  \end{minipage}}
    \end{picture}}%

  \renewcommand{\topfraction}{0.85}
  \renewcommand{\textfraction}{0.1}
  \renewcommand{\floatpagefraction}{0.75}
}

\fancypagestyle{pageCouverture}{
  \fancyhf{}
  \renewcommand{\footrulewidth}{0pt}
  \renewcommand{\headrulewidth}{0pt}

}


% \newcommand{\StylePageNormal}[0]
% {\lhead{\itshape \titreDetaillePrive : \referenceVersionPrive}
%   %   \rhead{\itshape \today}
%   \rhead{}
%   \renewcommand{\headrulewidth}{0.2pt}

%   %   --- Pieds ---
%   %   \lfoot{\footnotesize \textit{\departement{} - INSA - Tous droits réservés}}
%   \lfoot{ \scriptsize \textit{Ce document est la propriété du département \departement{} de l'INSA-Rouen et ne peut} \newline \scriptsize \textit{être ni reproduit, ni communiqué à des tiers sans autorisation préalable}}
%   \rfoot{Page \thepage{ }sur \pageref{LastPage}}
%   \renewcommand{\footrulewidth}{0.2pt}

%   %   --- Figures ----
%   \renewcommand{\topfraction}{0.85}
%   \renewcommand{\textfraction}{0.1}
%   \renewcommand{\floatpagefraction}{0.75}
% }

% Redefinition des chapitres
\makeatletter
\def\@makechapterhead#1{% chapter
  \vspace*{5\p@}%
  {\parindent \z@ \raggedright \normalfont
    \ifnum \c@secnumdepth >\m@ne
   \huge\bfseries \@chapapp\space \thechapter
   \par\nobreak
   \vskip 5\p@
    \fi
    \interlinepenalty\@M
    \Huge \bfseries #1\par\nobreak
    \vskip 10\p@
    \thispagestyle{fancy}% Permet d'ajouter l'entête de pied de page
  }}

\def\@schapter#1{\if@twocolumn
  \@topnewpage[\@makeschapterhead{#1}]%
  \else
  \@makeschapterhead{#1}%
  \@afterheading
  \fi}

\def\@makeschapterhead#1{% chapter*
  \vspace*{5\p@}%
  {\parindent \z@ \raggedright
    \normalfont
    \interlinepenalty\@M
    \Huge \bfseries  #1\par\nobreak
    \vskip 10\p@
    \thispagestyle{fancy} % Permet d'ajouter l'entête de pied de page
  }}
\renewcommand{\fnum@table}{Tableau \thetable}
\makeatother


% Parametres des pages de garde
% Premiere page
\newcommand{\titreAcronymePrive}[0]{\ }
\newcommand{\titreAcronyme}[1]{
  \renewcommand{\titreAcronymePrive}[0]{#1}
}
\newcommand{\titreGeneralPrive}[0]{\ }
\newcommand{\titreGeneral}[1]{
  \renewcommand{\titreGeneralPrive}[0]{#1}
}
\newcommand{\sousTitreGeneralPrive}[0]{\ }
\newcommand{\sousTitreGeneral}[1]{
  \renewcommand{\sousTitreGeneralPrive}[0]{#1}
}
\newcommand{\versionPrive}[0]{\ }
\newcommand{\version}[1]{
  \def\@version{#1}
  \renewcommand{\versionPrive}[0]{#1}
}
\newcommand{\titreDetaillePrive}[0]{\ }
\newcommand{\titreDetaille}[1]{
  \renewcommand{\titreDetaillePrive}[0]{#1}
}
% Deuxieme page
\newcommand{\auteursPrive}[0]{\ }
\newcommand{\auteurs}[1]{
  \renewcommand{\auteursPrive}[0]{#1}
}
\newcommand{\destinatairesPrive}[0]{\ }
\newcommand{\destinataires}[1]{
  \renewcommand{\destinatairesPrive}[0]{#1}
}
\newcommand{\resumePrive}[0]{\ }
\newcommand{\resume}[1]{
  \renewcommand{\resumePrive}[0]{#1}
}
\newcommand{\motsClesPrive}[0]{\ }
\newcommand{\motsCles}[1]{
  \renewcommand{\motsClesPrive}[0]{#1}
}
\newcommand{\natureDerniereModificationPrive}[0]{\ }
\newcommand{\natureDerniereModification}[1]{
  \renewcommand{\natureDerniereModificationPrive}[0]{#1}
}
\newcommand{\referenceVersionPrive}[0]{\ }
\newcommand{\referenceVersion}[1]{
  \renewcommand{\referenceVersionPrive}[0]{#1}
}
\newcommand{\modeDiffusionControlePrive}[0]{FAUX}
\newcommand{\modeDiffusionControle}[0]{
  \renewcommand{\modeDiffusionControlePrive}[0]{VRAI}
}
\newcommand{\modeDiffusionNonControle}[0]{
  \renewcommand{\modeDiffusionControlePrive}[0]{FAUX}
}

\fancypagestyle{pageIncluse}{
  \fancyhf{}
  \fancyfoot[CE]{
    \begin{picture}(0,0)
      \put(-510,-65){\includegraphics[width=350mm,height=25mm]{images/Volutes3.pdf}}
      \put(-270,-20){\begin{minipage}{5cm}\raggedleft Page \thepage \end{minipage}}
      %\put(-115,-40){\begin{minipage}{11cm} \raggedright \scriptsize \textit{\mentionLegale{}} \end{minipage}}
      \put(146,-42){\begin{minipage}{5cm}\raggedleft \footnotesize  \ifthenelse{\equal{\@version}{}}{}{version \@version}  \end{minipage}}
    \end{picture}}%

  \fancyfoot[CO]{
    \begin{picture}(0,0)
      \put(-485,-65){\includegraphics[width=350mm,height=25mm]{images/Volutes3bis.pdf}}
      \put(125,-20){Page \thepage}
      %\put(-200,-40){\begin{minipage}{11cm} \raggedleft \scriptsize \textit{\mentionLegale{}} \end{minipage}}
      \put(-285,-45){\begin{minipage}{5cm} \footnotesize  \ifthenelse{\equal{\@version}{}}{}{version \@version} \end{minipage}}
    \end{picture}}%
  \renewcommand{\topfraction}{0.85}
  \renewcommand{\textfraction}{0.1}
  \renewcommand{\floatpagefraction}{0.75}
}

\fancypagestyle{pageNormale}{ \fancyhf{}%
  \fancyhead[LE]{\itshape \titreDetaillePrive{} \referenceVersionPrive}
  \fancyhead[RO]{\itshape \titreDetaillePrive{} \referenceVersionPrive}
  \renewcommand{\headrulewidth}{0.2pt}

  \fancyfoot[CE]{
    \begin{picture}(0,0)
      \put(-510,-65){\includegraphics[width=350mm,height=25mm]{images/Volutes3.pdf}}
      \put(-270,-20){\begin{minipage}{5cm}\raggedleft Page \thepage \end{minipage}}
      %\put(-115,-40){\begin{minipage}{11cm} \raggedright \scriptsize \textit{\mentionLegale{}} \end{minipage}}
      \put(146,-42){\begin{minipage}{5cm}\raggedleft \footnotesize  \ifthenelse{\equal{\@version}{}}{}{version \@version}  \end{minipage}}
    \end{picture}}%

  \fancyfoot[CO]{
    \begin{picture}(0,0)
      \put(-485,-65){\includegraphics[width=350mm,height=25mm]{images/Volutes3bis.pdf}}
      \put(125,-20){Page \thepage}
      %\put(-200,-40){\begin{minipage}{11cm} \raggedleft \scriptsize \textit{\mentionLegale{}} \end{minipage}}
      \put(-285,-45){\begin{minipage}{5cm} \footnotesize  \ifthenelse{\equal{\@version}{}}{}{version \@version} \end{minipage}}
    \end{picture}}%

  \renewcommand{\topfraction}{0.85}
  \renewcommand{\textfraction}{0.1}
  \renewcommand{\floatpagefraction}{0.75}
}

\fancypagestyle{pageCouverture}{
  \fancyhf{}
  \renewcommand{\footrulewidth}{0pt}
  \renewcommand{\headrulewidth}{0pt}

}

% Troisieme Page (Pages de service)
\newenvironment{pagesService}
{%\addcontentsline{toc}{chapter}{Page de service}
  % \StylePageNormal
  % \thispagestyle{fancy}
  \addcontentsline{toc}{chapter}{Page de service}
  \chapter*{Page de service}
}
{}


\newenvironment{historique}
{\section*{Historique des évolutions}
  \begin{center}
    \begin{longtable}{|p{0.1\textwidth}|p{0.12\textwidth}|p{0.35\textwidth}|p{0.15\textwidth}|p{0.15\textwidth}|}
      \hline  % Une ligne horizontale
      \rowcolor[gray]{.8}
      Version         & Date          & Auteur(s)       & Modification(s) & Partie(s) modifiée(s)\\
      \hline
      \endfirsthead
      \hline  % Une ligne horizontale
      \rowcolor[gray]{.8}
      Version         & Date          & Auteur(s)       & Modification(s) & Partie(s) modifiée(s)\\
      \hline
      \endhead
    }
    {\end{longtable}
  \end{center}}

\newcommand{\unHistorique}[5]
{#1 & #2  &  #3  & #4 & #5 \\
  \hline
}

\newenvironment{suiviDiffusions}
{\section*{Suivi des diffusions}
  \begin{center}
    \begin{longtable}{|p{0.1\linewidth}|p{0.12\linewidth}|p{0.7\linewidth}|}
      \hline
      \rowcolor[gray]{.8}
      Version         & Date          & Destinataire(s)\\
      \hline
      \endfirsthead
      \hline
      \rowcolor[gray]{.8}
      Version         & Date          & Destinataire(s)\\
      \hline
      \endhead
    }
    {  \end{longtable}
  \end{center}
  \emph{Toute remarque ou difficulté d'application du présent document est à signaler à la Direction Qualité des PIC.}
}
\newcommand{\unSuivi}[3]
{ #1 & #2  & #3 \\
  \hline}
\newenvironment{lesDestinataires}
{\vspace{-14.5pt}\begin{list}{}
    {\setlength{\leftmargin}{0pt}
      \setlength{\topsep}{0pt}
      \setlength{\parskip}{0pt}
      \setlength{\partopsep}{0pt}
      \setlength{\parsep}{0pt}
    }
  }
  {\end{list}}

\newenvironment{signatures}
{\section*{Signatures}
  \begin{center}
    \begin{longtable}{|p{0.15\linewidth}|p{0.17\linewidth}|p{0.25\linewidth}|p{0.12\linewidth}|p{0.2\linewidth}|}
      \hline
      \rowcolor[gray]{.8}
      & Fonction                                        & Nom & Date & Visa     \\
      \hline
      \endfirsthead
      \hline
      \rowcolor[gray]{.8}
      & Fonction                                        & Nom & Date & Visa     \\
      \hline
      \endhead
    }
    {    \hline
    \end{longtable}
  \end{center}}
\newcommand{\uneSignature}[4]
{ #1 & #2  & #3 & #4 &\\\hline}

\newenvironment{ficheDeRole}[1]
{
  \begin{center}
    \begin{tabular}{|13.5cm|}
      \hline
      \rowcolor[gray]{.8}
      Fiche de Rôle descriptive \\
      \hline
      \rowcolor[gray]{.6}
      #1 \\
      \hline
    }
    {
      \hline
    \end{tabular}
  \end{center}
}

% Documents applicables
\newenvironment{documentsApplicables}[1][Document]
{\section*{Documents Applicables}
  \emph{L'ensemble des documents listés ci--dessous sont consultables à la
    bibliothèque de l'\insaRouen{} sur le site du Madrillet.}
  \begin{center}}
  { \end{center}}
\newenvironment{listeDeDocumentsApplicables}[1][Version]
{\begin{longtable}{|p{0.20\linewidth}|p{0.20\linewidth}|p{0.60\linewidth}|}
    \hline
    \rowcolor[gray]{.8}
    #1  & Date & Document\\
    \hline
    \endfirsthead
    \hline
    \rowcolor[gray]{.8}
    #1 &  Date & Document\\
    \hline
    \endhead
  }
  {\end{longtable}}
\newcommand{\unDocumentApplicable}[3]
{ #1 & #2 & #3 \\
  \hline}

% Terminologie
\newenvironment{terminologie}[1][Terminologie]
{\section*{Terminologie}
  {\emph{Les tableaux suivants indiquent les différentes significations des termes et
      abbréviations utilisées dans ce document}}
  \begin{center}}
  { \end{center}}

\newenvironment{terminologieUnTableau}[1][Terminologie]
{\section*{Terminologie}
  {\emph{Le tableau suivant indique les différentes significations des termes et
      abbréviations utilisées dans ce document}}
  \begin{center}}
  { \end{center}}

\newenvironment{listeDeDefinitions}[1][Terme]
{\begin{longtable}{|p{0.20\linewidth}|p{0.80\linewidth}|}
    \hline
    \rowcolor[gray]{.8}
    #1 & Signification\\
    \hline
    \endfirsthead
    \hline
    \rowcolor[gray]{.8}
    #1 & Signification\\
    \hline
    \endhead
  }
  {\end{longtable}}
\newcommand{\uneTerminologie}[2]
{ #1 & #2 \\
  \hline}
\newenvironment{listeDAbreviations}[1][Abreviation]
{\begin{longtable}{|p{0.20\linewidth}|p{0.80\linewidth}|}
    \hline
    \rowcolor[gray]{.8}
    #1 & Signification\\
    \hline
    \endfirsthead
    \hline
    \rowcolor[gray]{.8}
    #1 & Signification\\
    \hline
    \endhead
  }
  {\end{longtable}}
\newcommand{\uneAbreviation}[2]
{ #1 & #2 \\
  \hline}

\newenvironment{documentsReference}[1][Document]
{\section*{Documents en référence}
 % \emph{L'ensemble des documents listés ci-dessous sont téléchargables sur le site \url{qualite.insa-rouen.fr}.}
  \begin{center}}
  { \end{center}}
\newenvironment{listeDeReferences}[1][Document]
{\begin{longtable}{|p{0.6\linewidth}|p{0.4\linewidth}|}
    \hline
    \rowcolor[gray]{.8}
    #1 & Référence\\
    \hline
    \endfirsthead
    \hline
    \rowcolor[gray]{.8}
    #1 & Référence\\
    \hline
    \endhead
  }
  {\end{longtable}}
\newcommand{\uneReference}[2]
{ #1 & #2 \\
  \hline}

\newenvironment{enregistrementsRequis}[1][Enregistrement]
{\section*{Gestion des documents requis par l'\isoNeufMilleUn{}}
  \emph{Le tableau suivant indique les documents de référence pour la gestion des enregistrements requis
    par l'\isoNeufMilleUn{}.}
  \begin{center}}
  { \end{center}}
\newenvironment{listeDEnregistrements}[1][Enregistrement]
{\begin{longtable}{|p{0.5\linewidth}|p{0.5\linewidth}|}
    \hline
    \rowcolor[gray]{.8}
    Paragraphe de la norme - Enregistrement & Référencé - emplacement\\
    \hline
    \endfirsthead
    \hline
    \rowcolor[gray]{.8}
    Paragraphe de la norme - Enregistrement & Référencé - emplacement\\
    \hline
    \endhead
  }
  {\end{longtable}}
\newcommand{\unEnregistrement}[2]
{ #1 & #2 \\
  \hline}

\newenvironment{proceduresDocumentees}[1][Activité]
{\section*{Procédures documentées}
  \emph{Le tableau ci-dessous indique l'emplacement des procédures documentées pour
    les six activitées requises par l'\isoNeufMilleUn{}}
  \begin{center}}
  { \end{center}}
\newenvironment{listeDeProcedures}[1][Activité]
{\begin{longtable}{|p{0.4\linewidth}|p{0.6\linewidth}|}
    \hline
    \rowcolor[gray]{.8}
    Paragraphe de la norme - Activité & Procédure documentée\\
    \hline
    \endfirsthead
    \hline
    \rowcolor[gray]{.8}
    Paragraphe de la norme - Activité & Procédure documentée\\
    \hline
    \endhead
  }
  {\end{longtable}}
\newcommand{\uneProcedure}[2]
{ #1 & #2 \\
  \hline}

\newenvironment{documentsEfficacite}[1][Document]
{\section*{Documents démontrant l'efficacité de la mise en oeuvre du SMQ}
  \emph{Le tableau suivant indique l'emplacement des documents nécessaires pour
    démontrer l'efficacité de la mise en oeuvre du SMQ}
  \begin{center}}
  { \end{center}}
\newenvironment{listeDeDocuments}[1][Document]
{\begin{longtable}{|p{0.4\linewidth}|p{0.6\linewidth}|}
    \hline
    \rowcolor[gray]{.8}
    Paragraphe de la norme - Document & Référentiel de l'\unite{}\\
    \hline
    \endfirsthead
    \hline
    \rowcolor[gray]{.8}
    Paragraphe de la norme - Document & Référentiel de l'\unite{}\\
    \hline
    \endhead
  }
  {\end{longtable}}
\newcommand{\unDocument}[2]
{ #1 & #2 \\
  \hline}



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 
%                                  % 
% Mise en page des pages de gardes %
%                                  % 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 



\ifthenelse{\boolean{departASI}}
{
  \newcommand{\EnteteCouverture}[0]
  {\noindent
    \begin{tabular}[t]{p{3cm}p{13cm}}
      \multirow{6}{3cm}{\flushleft{\includegraphics[width=3cm]{imagesCommun/logoinsa}}} & \\
      & \LARGE{\textbf{Département ASI}} \\
      & Institut National des Sciences Appliquées de Rouen \\
      & Avenue de l'Université - BP 08 - 76801 Saint-Etienne-du-Rouvray Cedex \\
      & Tél : 02 32 95 97 79 - Fax : 02 32 95 97 08 \\
      & Mail : \mailDep{} - Web : \siteDep{} \\
      & \\
      \cline{1-2}
      % \multicolumn{2}{p{16cm}}{\center{\includegraphics[width=4cm]{\cheminINPUTS IMAGES/logoasi}}} \\
    \end{tabular}

    \begin{tabular}[t]{p{8cm}p{8cm}}
      \center{\includegraphics[width=4cm]{imagesAsi/logoASI}} & \center{\includegraphics[height=2cm]{asi/imagesAsi/logoP3}}
    \end{tabular}
  }
}{}

\newcommand{\couverture}
{
	\thispagestyle{pageCouverture}
  	\begin{picture}(0,0)
    	\put(-70,-485){\includegraphics[width=190mm]{images/CruiseLiner.png}} %pour mettre une image de fond qui sera recouverte. 
    	\put(-75,-755){\includegraphics[width=211mm]{images/couverture.pdf}}
        %\put(30,-650){\includegraphics[width=30mm]{images/orange.jpg}}
    	\put(140,-620){\begin{minipage}{13cm} \Large \titreGeneralPrive \\ \textit{\Large \sousTitreGeneralPrive} \\ \textbf{\Large \titreDetaillePrive}\end{minipage}}
    	\put(-65,-600){\begin{minipage}{6.3cm} \Large \raggedleft \titreAcronymePrive\end{minipage}}
    	\put(370,-750){\begin{minipage}{5cm} \raggedleft \footnotesize \ifthenelse{\equal{\@version}{}}{}{version \@version} \end{minipage}}
  	\end{picture}
  	\newpage
  	\cleardoublepage
}

\newcommand{\informationsGenerales}[0]
{\noindent
  \begin{center}
    \begin{minipage}{18cm}
      \begin{tabular}{lp{8cm}} % {|c|c|c|}
        % \hline % Une ligne sur tout le tableau
        \cline{1-1}
        \textit{\bfseries{Auteur(s)}} & \textsc{\auteursPrive}\\
        &\\% (pour avoir une ligne vide)
        \cline{1-1}
        \textit{\bfseries{Destinataire(s)}} & \destinatairesPrive \\
        &\\% (pour avoir une ligne vide)
        \cline{1-1}
        \textit{\bfseries{Résumé}} & \resumePrive \\
        &\\
        \cline{1-1}
        \textit{\bfseries{Thème et mots clés}} & \motsClesPrive \\
        &\\
        \cline{1-1}
        \textit{\bfseries{Nature de la dernière modification}} & \natureDerniereModificationPrive \\
        &\\
        \cline{1-1}

        \textit{\bfseries{Référence}} & \referenceVersionPrive  \ifthenelse{\equal{\@version}{}}{}{, version \@version}\\
      \end{tabular}
    \end{minipage}
  \end{center}
  \vspace{3cm}
  \begin{tabular}[h]{p{0.1\linewidth}>{\centering}p{0.6\linewidth}p{0.3\linewidth}}
    & \hfill Exemplaire en diffusion contrôlée
    &\ifthenelse{\equal{\modeDiffusionControlePrive}{VRAI}}{$\boxtimes$}{$\square$}\\
    & \hfill Exemplaire en diffusion non contrôlée
    &\ifthenelse{\equal{\modeDiffusionControlePrive}{VRAI}}{$\square$}{$\boxtimes$}\\
  \end{tabular}
  \newpage
  \cleardoublepage
}
% \newlength{\largeurCartouche}
% \newcommand{\includegraphicsAvecCartouche}[5]
% {\setlength{\largeurCartouche}{#1}
%   \centering \footnotesize
%   \begin{tabular}[h]{|p{0.2\largeurCartouche}|p{0.1\largeurCartouche}>{\centering}p{0.4\largeurCartouche}p{0.1\largeurCartouche}|p{0.2\largeurCartouche}|}
%     \hline
%     \multicolumn{5}{|c|}{\includegraphics[width=\largeurCartouche]{#2}}\\
%     \hline    \rowcolor[gray]{.8}
%     N\OE UD: #3 & TITRE: & #4 & & N°: #5\\
%     \hline
%   \end{tabular}
% }

\newcommand{\insererPageExterne}[1]{
  \newpage
  \thispagestyle{empty} \setlength{\unitlength}{1cm}
  \begin{picture}(21,29.7)
    \put(-2.6,1.5){\includegraphics[width=21cm]{#1}}
  \end{picture}
}
 \newcommand{\siteQualite}{http://qualite.insa-rouen.fr}
 \newcommand{\siteASI}{http://asi.insa-rouen.fr}
\newcommand{\pageQuatriemeCouverture}[0]{
  \newpage
  % \FPupn\result{1 2 +}
  % \FPupn\result{\thepage 2 div 2 * \thepage -}
  % \ifthen{\equal{\result}{3}}{\newpage}
  \thispagestyle{empty}
  \begin{picture}(0,0)
    \put(-75,-755){\includegraphics[width=211mm]{images/couvertureBis.pdf}}
    \put(75, -550){
      \begin{normalsize}
        \begin{minipage}{10cm}
          \begin{tabular}{r}
            INSA Rouen\\
            Avenue de l'Université - BP 08\\
            76801 Saint-Etienne-du-Rouvray Cedex\\
            Tél : 02 32 95 97 79\\
            Fax : 02 32 95 97 08\\
            \textit{\siteQualite}\\
            \textit{\siteASI}\\
          \end{tabular}
        \end{minipage}
      \end{normalsize}}
    \put(320, -610){\includegraphics[scale=0.4]{images/logoASI.pdf}}
  \end{picture}
}

\newcommand{\annexes}[0]{
  \newpage
  \addcontentsline{toc}{chapter}{Annexes}
  \thispagestyle{fancy}
  \begin{center}
    \begin{minipage}{10cm}
      \vspace{3in}
      \begin{center}
        {\LARGE \bf Annexes}\\
      \end{center}
    \end{minipage}
  \end{center}
  \appendix
  \newpage
}


% \renewcommand{\contentsname}{\cleardoublepage Table des matières}

\AtBeginDocument{
  % \setmarginsrb{2cm}{1cm}{2cm}{1cm}{1cm}{0.5cm}{0.5cm}{1cm}
  % \pagestyle{fancy} %Style de page par defaut
  % \StylePageNormal
  % \fancyhf{}

  \pagestyle{pageNormale}

}
