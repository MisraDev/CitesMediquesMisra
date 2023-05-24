using DbLibrary.modelo;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AppEscripotoriGestióHorari.model
{
    public class FilaDataGrid
    {

        public FilaDataGrid(string hora, List<EspecialitatDB> especialitats, EspecialitatDB dilluns, EspecialitatDB dimarts, EspecialitatDB dimecres, EspecialitatDB dijous, EspecialitatDB divendres, EspecialitatDB dissabte, EspecialitatDB diumenge)
        {
            Hora = hora;
            Especialitats = especialitats;
            Dilluns = dilluns;
            Dimarts = dimarts;
            Dimecres = dimecres;
            Dijous = dijous;
            Divendres = divendres;
            Dissabte = dissabte;
            Diumenge = diumenge;
        }

        public String Hora { get; set; }
        public List<EspecialitatDB> Especialitats { get; set; }
        public EspecialitatDB Dilluns { get; set; }
        public EspecialitatDB Dimarts { get; set; }
        public EspecialitatDB Dimecres { get; set; }
        public EspecialitatDB Dijous { get; set; }
        public EspecialitatDB Divendres { get; set; }
        public EspecialitatDB Dissabte { get; set; }
        public EspecialitatDB Diumenge { get; set; }
    }
}
