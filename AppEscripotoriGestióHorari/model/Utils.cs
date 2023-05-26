using DbLibrary.modelo;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AppEscripotoriGestióHorari.model
{
    public class Utils
    {

        public static List<FilaDataGrid> GenerarLlistaEspecialitats(List<Hora> horari, List<FilaDataGrid> dades, ObservableCollection<EspecialitatDB> especialitats, ObservableCollection<AgendaMetgeDB> horariMetge)
        {
            EspecialitatDB dilluns = new EspecialitatDB();
            EspecialitatDB dimarts = new EspecialitatDB();
            EspecialitatDB dimecres = new EspecialitatDB();
            EspecialitatDB dijous = new EspecialitatDB();
            EspecialitatDB divendres = new EspecialitatDB();
            EspecialitatDB dissabte = new EspecialitatDB();
            EspecialitatDB diumenge = new EspecialitatDB();
            foreach (Hora hor in horari)
            {
                dilluns = especialitats[0];
                dimarts = especialitats[0];
                dimecres = especialitats[0];
                dijous = especialitats[0];
                divendres = especialitats[0];
                dissabte = especialitats[0];
                diumenge = especialitats[0];

                foreach (AgendaMetgeDB aM in horariMetge)
                {

                    TimeSpan horaEH = aM.Hora;
                    int hores = horaEH.Hours;
                    int minuts = horaEH.Minutes;
                    string shores = hores.ToString();
                    string sminuts = minuts.ToString();

                    if (minuts != 30)
                    {
                        sminuts = "00";
                    }
                    string hora = shores + ":" + sminuts;

                    if (hora.Equals(hor.HoraCol))
                    {
                        switch (aM.DiaSetmana)
                        {
                            case "LUNES":
                                dilluns = comprovaEspecilitat(especialitats, aM.CodiEspecialitat);
                                break;
                            case "MARTES":
                                dimarts = comprovaEspecilitat(especialitats, aM.CodiEspecialitat);
                                break;
                            case "MIERCOLES":
                                dimecres = comprovaEspecilitat(especialitats, aM.CodiEspecialitat);
                                break;
                            case "JUEVES":
                                dijous = comprovaEspecilitat(especialitats, aM.CodiEspecialitat);
                                break;
                            case "VIERNES":
                                divendres = comprovaEspecilitat(especialitats, aM.CodiEspecialitat);
                                break;
                            case "SABADO":
                                dissabte = comprovaEspecilitat(especialitats, aM.CodiEspecialitat);
                                break;
                            case "DOMINGO":
                                diumenge = comprovaEspecilitat(especialitats, aM.CodiEspecialitat);
                                break;
                        }
                    }
                }
                FilaDataGrid fila = new FilaDataGrid(hor.HoraCol, especialitats, dilluns, dimarts, dimecres, dijous, divendres, dissabte, diumenge);
                dades.Add(fila);
            }
            return dades;
        }

        public static EspecialitatDB comprovaEspecilitat(ObservableCollection<EspecialitatDB> especialitats, int codiEspecialitat)
        {
            EspecialitatDB esp = null;
            foreach (EspecialitatDB e in especialitats)
            {
                if (e.Codi == codiEspecialitat) { esp = e; }
            }
            return esp;
        }

        internal static List<FilaDataGrid> OmplirLLista(List<FilaDataGrid> dades)
        {
            List<FilaDataGrid> fotoLlista = new List<FilaDataGrid>();
            foreach (FilaDataGrid f in dades)
            {
                FilaDataGrid newf = new FilaDataGrid(f.Hora, f.Especialitats, f.Dilluns, f.Dimarts, f.Dimecres, f.Dijous, f.Divendres, f.Dissabte, f.Diumenge);
                fotoLlista.Add(newf);
            }
            return fotoLlista;
        }

        public static void DesarHorariMetge( List<FilaDataGrid> dades, List<FilaDataGrid> newDades, int codiMetge)
        {
            for (int i = 0; i < dades.Count; i++)
            {
                FilaDataGrid oldFila = dades[i];
                FilaDataGrid newFila = newDades[i];
                CompararFiles( oldFila, newFila, codiMetge);
            }

        }

        public static void CompararFiles( FilaDataGrid oldFila, FilaDataGrid newFila, int codiMetge)
        {
            string dia;
            string horaString = newFila.Hora;
            TimeSpan hora = TimeSpan.Parse(horaString);
            

            if (oldFila.Dilluns.Codi != newFila.Dilluns.Codi)
            {
                dia = "LUNES";
                AgendaMetgeDB newEH = new AgendaMetgeDB(codiMetge, hora, dia, newFila.Dilluns.Codi);
                AgendaMetgeDB oldEH = new AgendaMetgeDB(codiMetge, hora, dia, oldFila.Dilluns.Codi);
                if (oldFila.Dilluns.Codi == 0)
                {
                    AgendaMetgeDB.insertarAgendaMetges(newEH);
                }
                if (oldFila.Dilluns.Codi != 0 && newFila.Dilluns.Codi != 0)
                {
                    AgendaMetgeDB.updateAgendaMetges(oldEH, newEH);
                }
                if (newFila.Dilluns.Codi == 0)
                {
                    AgendaMetgeDB.deleteAgendaMetges(oldEH);
                }
            }

            if (oldFila.Dimarts.Codi != newFila.Dimarts.Codi)
            {
                dia = "MARTES";
                AgendaMetgeDB newEH = new AgendaMetgeDB(codiMetge, hora, dia, newFila.Dimarts.Codi);
                AgendaMetgeDB oldEH = new AgendaMetgeDB(codiMetge, hora, dia, oldFila.Dimarts.Codi);
                if (oldFila.Dimarts.Codi == 0)
                {
                    AgendaMetgeDB.insertarAgendaMetges(newEH);
                }
                if (oldFila.Dimarts.Codi != 0 && newFila.Dimarts.Codi != 0)
                {
                    AgendaMetgeDB.updateAgendaMetges(oldEH, newEH);
                }
                if (newFila.Dimarts.Codi == 0)
                {
                    AgendaMetgeDB.deleteAgendaMetges(oldEH);
                }
            }

            if (oldFila.Dimecres.Codi != newFila.Dimecres.Codi)
            {
                dia = "MIERCOLES";
                AgendaMetgeDB newEH = new AgendaMetgeDB(codiMetge, hora, dia, newFila.Dimarts.Codi);
                AgendaMetgeDB oldEH = new AgendaMetgeDB(codiMetge, hora, dia, oldFila.Dimarts.Codi);
                if (oldFila.Dimecres.Codi == 0)
                {
                    AgendaMetgeDB.insertarAgendaMetges(newEH);
                }
                if (oldFila.Dimecres.Codi != 0 && newFila.Dimecres.Codi != 0)
                {
                    AgendaMetgeDB.updateAgendaMetges(oldEH, newEH);
                }
                if (newFila.Dimecres.Codi == 0)
                {
                    AgendaMetgeDB.deleteAgendaMetges(oldEH);
                }
            }

            if (oldFila.Dijous.Codi != newFila.Dijous.Codi)
            {
                dia = "JUEVES";
                AgendaMetgeDB newEH = new AgendaMetgeDB(codiMetge, hora, dia, newFila.Dimarts.Codi);
                AgendaMetgeDB oldEH = new AgendaMetgeDB(codiMetge, hora, dia, oldFila.Dimarts.Codi);
                if (oldFila.Dijous.Codi == 0)
                {
                    AgendaMetgeDB.insertarAgendaMetges(newEH);
                }
                if (oldFila.Dijous.Codi != 0 && newFila.Dijous.Codi != 0)
                {
                    AgendaMetgeDB.updateAgendaMetges(oldEH, newEH);
                }
                if (newFila.Dijous.Codi == 0)
                {
                    AgendaMetgeDB.deleteAgendaMetges(oldEH);
                }
            }

            if (oldFila.Divendres.Codi != newFila.Divendres.Codi)
            {
                dia = "VIERNES";
                AgendaMetgeDB newEH = new AgendaMetgeDB(codiMetge, hora, dia, newFila.Dimarts.Codi);
                AgendaMetgeDB oldEH = new AgendaMetgeDB(codiMetge, hora, dia, oldFila.Dimarts.Codi);
                if (oldFila.Divendres.Codi == 0)
                {
                    AgendaMetgeDB.insertarAgendaMetges(newEH);
                }
                if (oldFila.Divendres.Codi != 0 && newFila.Divendres.Codi != 0)
                {
                    AgendaMetgeDB.updateAgendaMetges(oldEH, newEH);
                }
                if (newFila.Divendres.Codi == 0)
                {
                    AgendaMetgeDB.deleteAgendaMetges(oldEH);
                }
            }

            if (oldFila.Dissabte.Codi != newFila.Dissabte.Codi)
            {
                dia = "SABADO";
                AgendaMetgeDB newEH = new AgendaMetgeDB(codiMetge, hora, dia, newFila.Dimarts.Codi);
                AgendaMetgeDB oldEH = new AgendaMetgeDB(codiMetge, hora, dia, oldFila.Dimarts.Codi);
                if (oldFila.Dissabte.Codi == 0)
                {
                    AgendaMetgeDB.insertarAgendaMetges(newEH);
                }
                if (oldFila.Dissabte.Codi != 0 && newFila.Dissabte.Codi != 0)
                {
                    AgendaMetgeDB.updateAgendaMetges(oldEH, newEH);
                }
                if (newFila.Dissabte.Codi == 0)
                {
                    AgendaMetgeDB.deleteAgendaMetges(oldEH);
                }
            }

            if (oldFila.Diumenge.Codi != newFila.Diumenge.Codi)
            {
                dia = "DOMINGO";
                AgendaMetgeDB newEH = new AgendaMetgeDB(codiMetge, hora, dia, newFila.Dimarts.Codi);
                AgendaMetgeDB oldEH = new AgendaMetgeDB(codiMetge, hora, dia, oldFila.Dimarts.Codi);
                if (oldFila.Diumenge.Codi == 0)
                {
                    AgendaMetgeDB.insertarAgendaMetges(newEH);
                }
                if (oldFila.Diumenge.Codi != 0 && newFila.Diumenge.Codi != 0)
                {
                    AgendaMetgeDB.updateAgendaMetges(oldEH, newEH);
                }
                if (newFila.Diumenge.Codi == 0)
                {
                    AgendaMetgeDB.deleteAgendaMetges(oldEH);
                }
            }

        }
    }
}
