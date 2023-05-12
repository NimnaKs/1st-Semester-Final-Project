package lk.ijse.posm.util.GenerateReports;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import lk.ijse.posm.Launcher;
import lk.ijse.posm.dto.ProductReportDTO;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

public class GeneratePdfMoneyTransfer {

    private String custId;

    private String custName;

    private String payemntId;

    private String custTp;

    private String sellingMoneyType;

    private String receivingMoneyType;

    private double sellingMoneyAmount;

    private double receivingMoneyAmount;

    public void setPayemntId(String payemntId) {
        this.payemntId = payemntId;
    }

    public void setCustTp(String custTp) {
        this.custTp = custTp;
    }

    public void setSellingMoneyType(String sellingMoneyType) {
        this.sellingMoneyType = sellingMoneyType;
    }

    public void setReceivingMoneyType(String receivingMoneyType) {
        this.receivingMoneyType = receivingMoneyType;
    }

    public void setSellingMoneyAmount(double sellingMoneyAmount) {
        this.sellingMoneyAmount = sellingMoneyAmount;
    }

    public void setReceivingMoneyAmount(double receivingMoneyAmount) {
        this.receivingMoneyAmount = receivingMoneyAmount;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void generateMoneyTrasferReport(){
        String path="invoiceMoneyTrasfer.pdf";
        PdfWriter pdfWriter = null;
        try {
            pdfWriter = new PdfWriter(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PdfDocument pdfDocument=new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document=new Document(pdfDocument);
        String imagePath="/Users/mac/Desktop/Post Office Management System 2/src/main/resources/assert/ReviewBackgroundForPdf.png";
        ImageData imageData= null;
        try {
            imageData = ImageDataFactory.create(imagePath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Image image=new Image(imageData);

        float x=pdfDocument.getDefaultPageSize().getWidth()/2;
        float y=pdfDocument.getDefaultPageSize().getHeight()/2;
        image.setFixedPosition(x-100,y-55);
        image.setOpacity(0.1f);
        document.add(image);

        float threeCol=198f;
        float twoCol=285f;
        float twoCol150=twoCol+150f;
        float twoColumnWidth[]={twoCol150,twoCol};
        float fullWidth[]={threeCol*3};
        float threeColumnWidth[]={threeCol,threeCol,threeCol};

        Paragraph oneSpace=new Paragraph("\n");

        Table table=new Table(twoColumnWidth);
        table.addCell(new Cell().add("Invoice").setFontSize(28f).setBorder(Border.NO_BORDER).setBold());
        Table nestedTable=new Table(new float[]{twoCol/2,twoCol/2});
        nestedTable.addCell(getHeaderTextCell("Invoice No."));
        nestedTable.addCell(getHeaderTextCellValue(payemntId));
        nestedTable.addCell(getHeaderTextCell("Invoice Date"));
        nestedTable.addCell(getHeaderTextCellValue(String.valueOf(Launcher.date)));

        table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));

        Border gb=new SolidBorder(Color.GRAY,2f);
        Table divider=new Table(fullWidth);
        divider.setBorder(gb);

        document.add(table);
        document.add(oneSpace);
        document.add(divider);
        document.add(oneSpace);

        Table twoColTable=new Table(twoColumnWidth);
        twoColTable.addCell(getBillingAndCustomerCells("Post Office Information"));
        twoColTable.addCell(getBillingAndCustomerCells("Customer Information"));
        document.add(twoColTable.setMarginBottom(12f));

        Table twoColTable2=new Table(twoColumnWidth);
        twoColTable2.addCell(getCell10Left("Post Office SriLanka",true));
        twoColTable2.addCell(getCell10Left("Customer Id",true));
        twoColTable2.addCell(getCell10Left("Kalutara Branch",false));
        twoColTable2.addCell(getCell10Left(custId,false));
        twoColTable2.addCell(getCell10Left("Address",true));
        twoColTable2.addCell(getCell10Left("Customer Name",true));
        twoColTable2.addCell(getCell10Left("Kalutara,KT1 1KB",false));
        twoColTable2.addCell(getCell10Left(custName,false));
        twoColTable2.addCell(getCell10Left("Telephone No",true));
        twoColTable2.addCell(getCell10Left("Customer Telephone No",true));
        twoColTable2.addCell(getCell10Left("034222650",false));
        twoColTable2.addCell(getCell10Left(custTp,false));
        twoColTable2.addCell(getCell10Left("Email Address",true));
        twoColTable2.addCell(getCell10Left("",true));
        twoColTable2.addCell(getCell10Left("postOfficeKaluataraKT1KB1SL@gmail.com",false));
        twoColTable2.addCell(getCell10Left("",false));
        document.add(twoColTable2);

        float oneColumnWidth[]={twoCol150};

//        Table oneColTable1=new Table(oneColumnWidth);
//        oneColTable1.addCell(getCell10Left("Email Address",true));
//        oneColTable1.addCell(getCell10Left("postOfficeKaluataraKT1KB1SL@gmail.com",false));
//        document.add(oneColTable1.setMarginBottom(10f));

        Table tableDivider2=new Table(fullWidth);
        Border dgb=new DashedBorder(Color.GRAY,0.5f);
        document.add((tableDivider2).setBorder(dgb));

        Paragraph productPara=new Paragraph("\n");
        document.add(productPara.setBold());

        Table threeColTable1=new Table(fullWidth);
        threeColTable1.setBackgroundColor(Color.BLACK,0.7f);
        threeColTable1.addCell(new Cell().add("Bill Information").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
        document.add(threeColTable1);

//        Table threeColTable2=new Table(threeColumnWidth);
//
//        float totalSum=0;
//        for (ProductReportDTO product : productList){
//            float total= (float) (product.getQty()*product.getPrice());
//            totalSum+=total;
//            threeColTable2.addCell(new Cell().add(product.getProductName()).setBorder(Border.NO_BORDER)).setMarginBottom(10f);
//            threeColTable2.addCell(new Cell().add(String.valueOf(product.getQty())).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
//            threeColTable2.addCell(new Cell().add(String.valueOf(total)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)).setMarginBottom(15f);
//        }
//
//        document.add(threeColTable2.setMarginBottom(20f));
//
//        float oneTwo[]={threeCol+125f,threeCol*2};
//        Table threeColTable4=new Table(oneTwo);
//        threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
//        threeColTable4.addCell(new Cell().add(tableDivider2).setBorder(Border.NO_BORDER));
//        document.add(threeColTable4);
//
//        Table threeColTable3=new Table(threeColumnWidth);
//        threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setMarginBottom(10f));
//        threeColTable3.addCell(new Cell().add("Total").setBold().setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
//        threeColTable3.addCell(new Cell().add(String.valueOf(totalSum)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)).setMarginBottom(10f);
//
//        document.add(threeColTable3);

        Table twoColTable3=new Table(twoColumnWidth);

        twoColTable3.addCell(getCell10Left("Selling Money Type",true));
        twoColTable3.addCell(getCell10Left(sellingMoneyType,true));
        twoColTable3.addCell(getCell10Left("Selling Money Amount",false));
        twoColTable3.addCell(getCell10Left(sellingMoneyAmount+"",false));
        twoColTable3.addCell(getCell10Left("Receiving Money Type",true));
        twoColTable3.addCell(getCell10Left(receivingMoneyType,true));
        twoColTable3.addCell(getCell10Left("Receiving Money Amount",false));
        twoColTable3.addCell(getCell10Left(receivingMoneyAmount+"",false));
        document.add(twoColTable3);

        document.add(new Paragraph("\n"));
        document.add(divider.setBorder(new SolidBorder(Color.GRAY,1)).setMarginBottom(15f));

        Table tb=new Table(fullWidth);
        tb.addCell(new Cell().add("TERM AND CONDITIONS\n").setBold().setBorder(Border.NO_BORDER));
        tb.addCell(new Cell().add("All invoices for Money Transfer service purchased at the Post Office are to be paid in full at the time of purchase. We accept cash, credit/debit cards, and other forms of payment as deemed acceptable by the Post Office.").setBorder(Border.NO_BORDER));
        document.add(tb);

        document.close();
        System.out.println("Document is generated .");

    }

    private  Cell getHeaderTextCell(String textValue){
        return new Cell().add(textValue).setBold().setBorder(Border.NO_BORDER);
    }

    private  Cell getHeaderTextCellValue(String textValue){
        return new Cell().add(textValue).setBold().setBorder(Border.NO_BORDER);
    }

    private  Cell getBillingAndCustomerCells(String textValue){
        return new Cell().add(textValue).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    private  Cell getCell10Left(String textValue,Boolean isBold){
        Cell myCell=new Cell().add(textValue).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold?myCell.setBold():myCell;
    }
}
