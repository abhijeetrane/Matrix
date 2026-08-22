//Team: Tahiti
//Date: 2005/05/27
//Vers: 1.0
//File: ColumnData.java
package vista.ui;

/** This class is a container of column formatting information.<p>

    Member variables public to make accesing easier which is okay
    since this is a package-level class.
*/
class ColumnData
{
/** Column title */
public String  title;

/** Column width */
public int width;

/** Column alignment */
public int     alignment;

/** Constructor.
  @param title Column title.
  @param width Column width.
  @param alightment Column alignment. */
public ColumnData(String title, int width, int alignment) {
this.title = title;
this.width = width;
this.alignment = alignment;
}
}
