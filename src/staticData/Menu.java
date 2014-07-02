package staticData;

import java.util.HashMap;

public final class Menu {

    private static String[][][] menus =
                                      {
                                      {
                                      { "User" },
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
                                      { "Add Course" }
                                      },
                                      {
                                      { "Teacher" },
                                      { "Display Teachers" },
                                      { "Add Teacher" }
                                      },
                                      {
                                      { "Student" },
                                      { "Display Students" },
                                      { "Add Student" }
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

    public static HashMap<Integer, String> list()
    {
        HashMap<Integer, String> mapMenus = new HashMap<Integer, String>();
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
