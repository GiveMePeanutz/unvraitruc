package staticData;

import java.util.LinkedHashMap;

public final class Menu {

    private static String[][][] menus =
                                      {
                                      {
                                      { "User" , "Delete User" , "Modify User" },
                                      { "Display Users" },
                                      { "Add User" },
                                      { "Group",
                                      "Display Groups",
                                      "Add Group"
                                      },
                                      { "Privilege",
                                      "Display Privileges",
                                      "Add Privilege"
                                      }
                                      },
                                      {
                                      { "Course" },
                                      { "Display Courses" },
                                      { "Add Course" },
                                      { "Delete Course" },
                                      { "Modify Course" }
                                      },
                                      {
                                      { "Teacher" , "Delete Teacher" , "Modify Teacher" },
                                      { "Display Teachers" },
                                      { "Add Teacher" },
                                      { "Delete Teacher" },
                                      { "Modify Teacher" }
                                      },
                                      {
                                      { "Student" , "Delete Student" , "Modify Student"},
                                      { "Display Students" },
                                      { "Add Student" },
                                      { "Delete Student" },
                                      { "Modify Student" }
                                      },
                                      {
                                      { "Warehouse Maintenance" }
                                      },
                                      {
                                      { "Data Analysis" }
                                      },
                                      {
                                      { "Profile" }
                                      }
                                      };

    public static String[][][] getMenus() {
        return menus;
    }

    public static String getMenuName( int path ) {

        return menus[path / 10000][( path - ( path / 10000 ) * 10000 ) / 100][( path - ( path / 100 ) * 100 )];
    }

    public static LinkedHashMap<Integer, String> list()
    {
        LinkedHashMap<Integer, String> mapMenus = new LinkedHashMap<Integer, String>();
        for ( int i = 0; i < menus.length; i++ )
        {

            for ( int j = 0; j < menus[i].length; j++ )
            {
                for ( int k = 0; k < menus[i][j].length; k++ )
                {

                    mapMenus.put( getPath( i, j, k ), getMenuName( getPath( i, j, k ) ) );
                }
            }
        }

        return mapMenus;
    }

    public static int getPath( int firstmenu, int submenu, int subsubmenu ) {

        return ( firstmenu * 10000 + submenu * 100 + subsubmenu );
    }

}
