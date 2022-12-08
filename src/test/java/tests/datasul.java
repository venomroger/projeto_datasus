//Autores: Roger Macedo Drumond e  Reinaldo Correia Santos
//Versão: 1.0
//Data: 01/12/2022
//Objetivo:Buscar o resultado no site CNES de HOSPITAL e Salva-lo em Excell, filtrado por Estado e Gestão
package tests;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.WebDriverFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class datasul {
    final static Logger logger = LoggerFactory.getLogger(datasul.class);
    static private WebDriver browser;
    private static final String filename = "C:\\Users\\rmacedod\\Downloads\\datasus\\teste.xls";

    static private Wait<WebDriver> wait;

    @BeforeAll
    static public void configuration() {
        browser = WebDriverFactory.createChromeBrowser();
        browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        wait = new FluentWait<WebDriver>(browser).withTimeout(Duration.ofSeconds(60))
                .pollingEvery(Duration.ofSeconds(3)).ignoring(NoSuchElementException.class);
    }

    @Test
    public void myTest() throws InterruptedException {
        browser.get("https://cnes.datasus.gov.br/");
        browser.findElement(By.xpath("//a[normalize-space()='Consultas']")).click();
        browser.findElement(By.xpath("//a[normalize-space()='Estabelecimentos']")).click();
        browser.findElement(By.xpath("//a[normalize-space()='Identificação']")).click();


        //ComboBox de ESTADO
        WebElement estadoElement = browser.findElement(By.tagName("select"));
        Select estado = new Select(estadoElement);
        estado.selectByVisibleText("ALAGOAS");

        //ComboBox de Gestão
        WebElement gestaoElement = browser.findElement(By.xpath("//select[@ng-model='Gestao']"));
        Select gestao = new Select(gestaoElement);
        gestao.selectByVisibleText("MUNICIPAL");

        browser.findElement(By.xpath("//input[@id='pesquisaValue']")).sendKeys("HOSPITAL");
        browser.findElement(By.xpath("//button[normalize-space()='Pesquisar']")).click();

        //Paginaçao resultado pesquisa INICIO
        List<WebElement> paginacaolista = browser.findElements(By.xpath("//ul[@ng-if='pages.length']/li/a"));
        //System.out.println(paginacaolista.size());
        String numeropaginas = "1";
        if(Integer.parseInt(String.valueOf(paginacaolista.size()))>0){
            numeropaginas = browser.findElement(By.xpath("(//ul[@ng-if='pages.length']/li/a)["+(paginacaolista.size()-1)+"]")).getText();
        }
        //System.out.println(numeropaginas);
        List<Data> listadata = new ArrayList<Data>();

        for(int i=1; i<=Integer.parseInt(numeropaginas); i++){

            if(i>=2){
                //System.out.println("Pagina: "+ i );
                WebElement nextPageButton = browser.findElement(By.xpath("(//ul[@ng-if='pages.length']/li/a)["+(paginacaolista.size())+"]"));
                ((JavascriptExecutor) browser).executeScript("arguments[0].scrollIntoView(true);", nextPageButton);
                Thread.sleep(500);
                nextPageButton.click();
            }
            //Paginaçao resultado pesquisa FIM

            List<WebElement> trssize = browser.findElements(By.xpath("//tbody/tr"));
            int nomes = trssize.size();
            for(int index=1;index<=nomes;index++){
                String uf = browser.findElement(By.xpath("//tbody/tr["+index+"]/td[1]")).getText();
                String municipio = browser.findElement(By.xpath("//tbody/tr["+index+"]/td[2]")).getText();
                String cnes = browser.findElement(By.xpath("//tbody/tr["+index+"]/td[3]")).getText();
                String nomefantasia = browser.findElement(By.xpath("//tbody/tr["+index+"]/td[4]")).getText();
                String natureza = browser.findElement(By.xpath("//tbody/tr["+index+"]/td[5]")).getText();
                String gestao1 = browser.findElement(By.xpath("//tbody/tr["+index+"]/td[6]")).getText();
                String atendesus = browser.findElement(By.xpath("//tbody/tr["+index+"]/td[7]")).getText();
                listadata.add(new Data(uf,municipio,cnes,nomefantasia,natureza,gestao1,atendesus));

            }
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheetUsers = workbook.createSheet("Estabelecimentos");
        int rownum = 0;
        for (Data data : listadata) {
            Row row = sheetUsers.createRow(rownum++);
            int cellnum = 0;
            Cell celluf = row.createCell(cellnum++);
            celluf.setCellValue(data.getUf());
            Cell cellmunicipio = row.createCell(cellnum++);
            cellmunicipio.setCellValue(data.getMunicipio());
            Cell cellcnes = row.createCell(cellnum++);
            cellcnes.setCellValue(data.getCnes());
            Cell cellnomefantasia = row.createCell(cellnum++);
            cellnomefantasia.setCellValue(data.getNomefantasia());
            Cell cellnatureza = row.createCell(cellnum++);
            cellnatureza.setCellValue(data.getNatureza());
            Cell cellgestao = row.createCell(cellnum++);
            cellgestao.setCellValue(data.getGestao());
            Cell cellatendesus = row.createCell(cellnum++);
            cellatendesus.setCellValue(data.getAtendesus());
        }

        try {
            FileOutputStream out =
                    new FileOutputStream(new File(datasul.filename));
            workbook.write(out);
            out.close();
            System.out.println("Arquivo Excel criado com sucesso!");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Arquivo não encontrado!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro na edição do arquivo!");
        }

        browser.quit();
    }

}
