# Data Files
## Filename Extension 
- .dff - data file format
- .dmf - data memory format
- .dfa - data file access
- .ifa - index file access
```flow
st=>start: Start:>http://google.com[blank]
e=>end:>http://www.google.com
opl=>operation: My Operation
subl=>subroutine: My Subroutine
cond=>condition: Yes
or No?:>http://www.goggle.com
io=>inputoutput: catch something...

st->opl->cond
cond(yes)->io->e
cond(no)->subl(right)->opl
```