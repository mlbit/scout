# Data Files
## Filename Extension 
- DFF (.dff) - Data File Format provides information on how the data are stored in Data File Content (.dfc).  
- DMF (.dmf) - Data Memory Format provides information on how the data are stored in memory.
- DFC (.dfc) - Data File Content store the actual data stored in the file system.
- IFC (.ifc) - Index File Content store the record index of the file content in DFC. This file can be regenerated from the actual content file.
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