\NeedsTeXFormat{LaTeX2e}[1995/12/01]
\ProvidesPackage{TesiDiaUniroma3}[2008/02/13 v1.0 Tesi - DIA-Uniroma3]

\LoadClass[a4paper,11pt,oneside,italian]{article}

% Si utilizza la sillabazione italiana
\RequirePackage[italian]{babel}
% Load graphicx
\RequirePackage{graphicx}
% Line spacing
\RequirePackage{setspace}

% pacchetti per i font
\usepackage[T1]{fontenc}
% interlinea
\linespread{1}
% margini (la dimensione del blocco di testo viene regolata di conseguenza)
% \setlrmarginsandblock{3.5cm}{3cm}{*}
% profondità dell'indice (settato per stampare fino alle sottosezioni)
\setcounter{tocdepth}{2}
% numera parti, capitoli, sezioni, sottosezioni e sotto-sottosezioni
% \maxsecnumdepth{subsubsection}
% \setsecnumdepth{subsubsection}

% Autore 
\newcommand{\@autore}{Autore}
\newcommand{\autore}[1]{\renewcommand{\@autore}{#1}}

% Titolo
\newcommand{\@titolo}{Titolo della tesi}
\newcommand{\titolo}[1]{\renewcommand{\@titolo}{#1}}

% Matricola 
\newcommand{\@matricola}{000000}
\newcommand{\matricola}[1]{\renewcommand{\@matricola}{#1}}

% Github
\newcommand{\@github}{link github}
\newcommand{\github}[1]{\renewcommand{\@github}{\href{#1}{#1}}}

% Generazione del FRONTESPIZIO
\newcommand{\generaFrontespizio}{
   \thispagestyle{empty}
   \begin{center}
      \vspace{10mm}
      {\includegraphics[width=0.4 \linewidth]{img/uniroma3-logo}} \\
      \vspace{10mm}
      {\Large UNIVERSIT\`A DEGLI STUDI ROMA TRE} \\
      \vspace{5mm}
      % {\LARGE Dipartimento di Ingegneria} \\
      % {\Large Corso di Laurea in Ingegneria Informatica} \\
      \vspace{15mm}
      {\LARGE Ingegneria dei dati: homework 6} \\
      \vspace{15mm}
      {\huge \@titolo} \\
      \vspace{15mm}
      {\textbf{\@autore} \\ Matricola \@matricola} \\
      % \vspace{15mm}
      % {\begin{figure}[h]
      %   \centering
      %       \begin{tabular}{m{1cm}c}
      %       \includegraphics[scale=0.01]{tesina/img/GitHub-logo.png} & \@github\\
      %       \end{tabular}
      %  \end{figure}
      % } \\
      % \vspace{7mm}
      \vfill
      {\large \today} \\
   \end{center}
   \newpage 
}

% Comandi riguardanti l'inserimento di capitoli
\newcommand{\sezione}[2]{\section{#1}\label{sez:#2}\input{#2}}

% Comandi riguardanti la generazione di indici
\newcommand{\generaIndice}{
	%\fancyhead[L]{\textbf{\rightmark}}
	\newpage
	\tableofcontents
	% \clearpage
	%\fancyhead[L]{\textbf{\leftmark}}
}
\newcommand{\generaIndiceTabelle}{\listoftables}
\newcommand{\generaIndiceFigure}{\listoffigures \newpage}

% Bibliografia in ordine alfabetico
\let \OldBib \bibliography
\renewcommand{\bibliography}[1]{\newpage \markboth{Bibliografia}{Bibliografia} \OldBib{#1}}
\bibliographystyle{abbrv}
