using System.Diagnostics;
using System.Reflection;

[assembly: AssemblyVersion("1.0.0.0")]
[assembly: AssemblyFileVersion ("1.0.0.0")]
[assembly: AssemblyInformationalVersion("1.0")]
[assembly: AssemblyTitle("TakoImageEditor")]
[assembly: AssemblyDescription("画像編集ソフトです")]
[assembly: AssemblyProduct("TakoImageEditor")]
[assembly: AssemblyCulture("")]

public class App {
    public static void Main(string[] args) {
        ProcessStartInfo processStartInfo = new ProcessStartInfo("minjre\\bin\\javaw.exe", "-jar TakoImageEditor.jar");
        Process.Start(processStartInfo);
    }
}