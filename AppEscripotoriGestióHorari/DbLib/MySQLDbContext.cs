using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Text;

namespace DBLib
{
    class MySQLDbContext : DbContext
    {
        protected override void OnConfiguring(DbContextOptionsBuilder optionBuilder)
        {
            optionBuilder.UseMySQL("Server=51.68.224.27;Port=3306;Database=dam2_mdelaro4;Uid=dam2_mdelaro4;Pwd=6673C;");
        }
    }  
}
