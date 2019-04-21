package com.bconnect.b_connect;

import android.content.Context;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class WebServiceCall {
    private final String NAMESPACE = "http://tempuri.org/";
    // private  final  String URL="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx";
    private String URL;

    private Context context;


    public WebServiceCall(Context context) {
        this.context = context;
    }

    //    private String webResponce="";
//    private Thread thread;
    SharedPreferencesSR SPsr = new SharedPreferencesSR();


    public void Login(String name, String pass) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";
            Log.e("webservicecall", "URLSOAP " + URL);
        }

        //url = http://172.16.0.2/webser/android.asmx
        // https://www.freeformatter.com/xml-to-json-converter.html
        //http://bconnect-egypt.info/
        //http://41.187.17.242
        //final String NAME_SPACE = "http://tempuri.org/;

        final String METHOD_NAME = "Login";
        final String SOAP_ACTION = "http://tempuri.org/Login";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", name);
        request.addProperty("password", pass);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();
            WebServiceResult.eCode = result.getProperty("eCode").toString();
            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            // e.printStackTrace();
            WebServiceResult.eCode = "0";
            WebServiceResult.ErrorID = 1;
        }
    }

    public void getUserData(String emp) {


        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";
        }

        final String METHOD_NAME = "getuserdata";
        final String SOAP_ACTION = "http://tempuri.org/getuserdata";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("emp", emp);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceUserData.code = result.getProperty("code").toString();
            WebServiceUserData.name = result.getProperty("name").toString();
            WebServiceUserData.emp_address = result.getProperty("emp_address").toString();
            WebServiceUserData.emp_mob1 = result.getProperty("emp_mob1").toString();
            WebServiceUserData.emp_mob2 = result.getProperty("emp_mob2").toString();
            WebServiceUserData.emp_tel1 = result.getProperty("emp_tel1").toString();
            WebServiceUserData.emp_tel2 = result.getProperty("emp_tel2").toString();
            WebServiceUserData.emp_city = result.getProperty("emp_city").toString();
            WebServiceUserData.dep_code = result.getProperty("dep_code").toString();
            WebServiceUserData.pos_code = result.getProperty("pos_code").toString();

            WebServiceUserData.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceUserData.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }

    }

    public void GetClientData(String motaheda_code) {
        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }

        final String METHOD_NAME = "GetClientData";
        final String SOAP_ACTION = "http://tempuri.org/GetClientData";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("search", motaheda_code);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceClientData.code = result.getProperty("code").toString();
            WebServiceClientData.phar_name = result.getProperty("phar_name").toString();
            WebServiceClientData.address = result.getProperty("address").toString();
            WebServiceClientData.tel1 = result.getProperty("tel1").toString();
            WebServiceClientData.tel2 = result.getProperty("tel2").toString();
            WebServiceClientData.mob1 = result.getProperty("mob1").toString();
            WebServiceClientData.mob2 = result.getProperty("mob2").toString();
            WebServiceClientData.manager_name = result.getProperty("manager_name").toString();
            WebServiceClientData.motaheda_code = result.getProperty("motaheda_code").toString();
            WebServiceClientData.area_code = result.getProperty("area_code").toString();
            WebServiceClientData.approve_flag = result.getProperty("approve_flag").toString();
            WebServiceClientData.activation_flag = result.getProperty("activation_flag").toString();
            WebServiceClientData.stage_flag = result.getProperty("stage_flag").toString();
            WebServiceClientData.motaheda_branch = result.getProperty("motaheda_branch").toString();
            WebServiceClientData.created_by = result.getProperty("created_by").toString();
            WebServiceClientData.activated_by = result.getProperty("activated_by").toString();
            WebServiceClientData.approved_by = result.getProperty("approved_by").toString();
            WebServiceClientData.supervisor_code = result.getProperty("supervisor_code").toString();
            WebServiceClientData.another_name = result.getProperty("another_name").toString();
            WebServiceClientData.date_created = result.getProperty("date_created").toString();
            WebServiceClientData.date_activted = result.getProperty("date_activted").toString();
            WebServiceClientData.data_approved = result.getProperty("data_approved").toString();
            WebServiceClientData.snotes = result.getProperty("snotes").toString();
            WebServiceClientData.contract_ser = result.getProperty("contract_ser").toString();
            WebServiceClientData.sent_flag = result.getProperty("sent_flag").toString();
            WebServiceClientData.super_notes = result.getProperty("super_notes").toString();
            WebServiceClientData.rep_code = result.getProperty("rep_code").toString();
            WebServiceClientData.master_phar_code = result.getProperty("master_phar_code").toString();
            WebServiceClientData.sent_date = result.getProperty("sent_date").toString();
            WebServiceClientData.web_s = result.getProperty("web_s").toString();
            WebServiceClientData.cs_returned_to_sale = result.getProperty("cs_returned_to_sale").toString();
            WebServiceClientData.cs_returned_flag = result.getProperty("cs_returned_flag").toString();
            WebServiceClientData.cs_who_return = result.getProperty("cs_who_return").toString();
            WebServiceClientData.ho_state = result.getProperty("ho_state").toString();
            WebServiceClientData.papers_count = result.getProperty("papers_count").toString();
            WebServiceClientData.e_mail = result.getProperty("e_mail").toString();


            WebServiceClientData.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceClientData.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
            WebServiceClientData.ErrorID = 1;

        }

    }

    public void GetClientData_Master_Code(String code) {
        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "GetClientData_Master_Code";
        final String SOAP_ACTION = "http://tempuri.org/GetClientData_Master_Code";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("code", code);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceClientData.code = result.getProperty("code").toString();
            WebServiceClientData.phar_name = result.getProperty("phar_name").toString();
            WebServiceClientData.address = result.getProperty("address").toString();

            WebServiceClientData.tel1 = result.getProperty("tel1").toString();
            WebServiceClientData.tel2 = result.getProperty("tel2").toString();
            WebServiceClientData.mob1 = result.getProperty("mob1").toString();
            WebServiceClientData.mob2 = result.getProperty("mob2").toString();
            WebServiceClientData.manager_name = result.getProperty("manager_name").toString();
            WebServiceClientData.motaheda_code = result.getProperty("motaheda_code").toString();
            WebServiceClientData.area_code = result.getProperty("area_code").toString();
            WebServiceClientData.approve_flag = result.getProperty("approve_flag").toString();
            WebServiceClientData.activation_flag = result.getProperty("activation_flag").toString();
            WebServiceClientData.stage_flag = result.getProperty("stage_flag").toString();
            WebServiceClientData.motaheda_branch = result.getProperty("motaheda_branch").toString();
            WebServiceClientData.created_by = result.getProperty("created_by").toString();
            WebServiceClientData.activated_by = result.getProperty("activated_by").toString();
            WebServiceClientData.approved_by = result.getProperty("approved_by").toString();
            WebServiceClientData.supervisor_code = result.getProperty("supervisor_code").toString();
            WebServiceClientData.another_name = result.getProperty("another_name").toString();
            WebServiceClientData.date_created = result.getProperty("date_created").toString();
            WebServiceClientData.date_activted = result.getProperty("date_activted").toString();
            WebServiceClientData.data_approved = result.getProperty("data_approved").toString();
            WebServiceClientData.snotes = result.getProperty("snotes").toString();
            WebServiceClientData.contract_ser = result.getProperty("contract_ser").toString();
            WebServiceClientData.sent_flag = result.getProperty("sent_flag").toString();
            WebServiceClientData.super_notes = result.getProperty("super_notes").toString();
            WebServiceClientData.rep_code = result.getProperty("rep_code").toString();
            WebServiceClientData.master_phar_code = result.getProperty("master_phar_code").toString();
            WebServiceClientData.sent_date = result.getProperty("sent_date").toString();
            WebServiceClientData.web_s = result.getProperty("web_s").toString();
            WebServiceClientData.cs_returned_to_sale = result.getProperty("cs_returned_to_sale").toString();
            WebServiceClientData.cs_returned_flag = result.getProperty("cs_returned_flag").toString();
            WebServiceClientData.cs_who_return = result.getProperty("cs_who_return").toString();
            WebServiceClientData.ho_state = result.getProperty("ho_state").toString();
            WebServiceClientData.papers_count = result.getProperty("papers_count").toString();
            WebServiceClientData.e_mail = result.getProperty("e_mail").toString();


            WebServiceClientData.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceClientData.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    public void GetClientDataMore(String cl_code) {
        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "GetClientDataMore";
        final String SOAP_ACTION = "http://tempuri.org/GetClientDataMore";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("cl_code", cl_code);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();


            WebServiceClientDataMore.cl_code = result.getProperty("cl_code").toString();
            WebServiceClientDataMore.cont_kind = result.getProperty("cont_kind").toString();
            WebServiceClientDataMore.monthly_fee = result.getProperty("monthly_fee").toString();
            WebServiceClientDataMore.prog_price = result.getProperty("prog_price").toString();
            WebServiceClientDataMore.setup_date = result.getProperty("setup_date").toString();
            WebServiceClientDataMore.down_pay = result.getProperty("down_pay").toString();
            WebServiceClientDataMore.remaining = result.getProperty("remaining").toString();
            WebServiceClientDataMore.old_prog = result.getProperty("old_prog").toString();
            WebServiceClientDataMore.old_prog_name = result.getProperty("old_prog_name").toString();
            WebServiceClientDataMore.direction_kind = result.getProperty("direction_kind").toString();
            WebServiceClientDataMore.client_supp = result.getProperty("client_supp").toString();
            WebServiceClientDataMore.phar_type = result.getProperty("phar_type").toString();
            WebServiceClientDataMore.client_kind = result.getProperty("client_kind").toString();
            WebServiceClientDataMore.banks = result.getProperty("banks").toString();
            WebServiceClientDataMore.b_disc = result.getProperty("b_disc").toString();
            WebServiceClientDataMore.prog_name = result.getProperty("prog_name").toString();
            WebServiceClientDataMore.add_item = result.getProperty("add_item").toString();


            WebServiceClientDataMore.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceClientDataMore.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    public void GetContractTypeDataByCode(String code) {
        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "GetContractTypeDataByCode";
        final String SOAP_ACTION = "http://tempuri.org/GetContractTypeDataByCode";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("code", code);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();


            WebServiceContTypeData.code = result.getProperty("code").toString();
            WebServiceContTypeData.name = result.getProperty("name").toString();
            WebServiceContTypeData.price = result.getProperty("price").toString();
            WebServiceContTypeData.down_pay = result.getProperty("down_pay").toString();
            WebServiceContTypeData.detail1 = result.getProperty("detail1").toString();
            WebServiceContTypeData.detail2 = result.getProperty("detail2").toString();

            WebServiceContTypeData.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceContTypeData.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    ///////SR: Methods For reactivate or new contract//
    //0
    public void GetReciept_ver(String reciept_code) {
        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "GetReciept_ver";
        final String SOAP_ACTION = "http://tempuri.org/GetReciept_ver";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("reciept_code", reciept_code);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();
            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }
    }

    //5
    public void Insert_Reciepts_Data(String reciept_code, String reciept_date) {
        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Insert_Reciepts_Data";
        final String SOAP_ACTION = "http://tempuri.org/Insert_Reciepts_Data";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("reciept_code", reciept_code);
        request.addProperty("reciept_date", reciept_date);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //6
    public void Insert_Contract_Fess(String cl_code, String cl_rep_code, String reciept_serial, String amount
            , String pay_way, String pay_notes, String cheque_ser, String cheque_date, String visit_date, String scode) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Insert_Contract_Fess";
        final String SOAP_ACTION = "http://tempuri.org/Insert_Contract_Fess";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("cl_code", cl_code);
        request.addProperty("cl_rep_code", cl_rep_code);
        request.addProperty("reciept_serial", reciept_serial);
        request.addProperty("amount", amount);
        request.addProperty("pay_way", pay_way);
        request.addProperty("pay_notes", pay_notes);
        request.addProperty("cheque_ser", cheque_ser);
        request.addProperty("cheque_date", cheque_date);
        request.addProperty("visit_date", visit_date);
        request.addProperty("scode", scode);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //4 in case branch
    public void Insert_Master_Phar_Mapping(String phar_code, String master_phar_name, String phar_name) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Insert_Master_Phar_Mapping";
        final String SOAP_ACTION = "http://tempuri.org/Insert_Master_Phar_Mapping";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("phar_code", phar_code);
        request.addProperty("master_phar_name", master_phar_name);
        request.addProperty("phar_name", phar_name);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //8
    public void Insert_Emp_Daily_Report(String rep_code, String phar_code, String doc_name, String visit_notes, String phar_name, String phar_phone, String branch_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Insert_Emp_Daily_Report";
        final String SOAP_ACTION = "http://tempuri.org/Insert_Emp_Daily_Report";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("phar_code", phar_code);
        request.addProperty("rep_code", rep_code);
        request.addProperty("doc_name", doc_name);
        request.addProperty("visit_notes", visit_notes);
        request.addProperty("phar_phone", phar_phone);
        request.addProperty("branch_code", branch_code);
        request.addProperty("phar_name", phar_name);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //2
    public void Insert_Activate_Data(String phar_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Insert_Activate_Data";
        final String SOAP_ACTION = "http://tempuri.org/Insert_Activate_Data";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("phar_code", phar_code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //3
    public void Insert_Reactivation_Phar(String rep_code, String re_year, String re_downpay, String re_contract_kind, String re_phar_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Insert_Reactivation_Phar";
        final String SOAP_ACTION = "http://tempuri.org/Insert_Reactivation_Phar";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("rep_code", rep_code);
        request.addProperty("re_year", re_year);
        request.addProperty("re_downpay", re_downpay);
        request.addProperty("re_contract_kind", re_contract_kind);
        request.addProperty("re_phar_code", re_phar_code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    public void GetSuper(String emp) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "GetSuper";
        final String SOAP_ACTION = "http://tempuri.org/GetSuper";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("emp", emp);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceSuperName.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceSuperName.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //1
    //SR: date yyyy-dd-MM//    //SR: date yyyy-dd-MM//    //SR: date yyyy-dd-MM//    //SR: date yyyy-dd-MM//    //SR: date yyyy-dd-MM//
    public void Update_cover_page_reactivate(String phar_code, String rep_code, String doc_name, String created_by, String supervisor_name, String snotes, String month_fee,
                                             String cont_kind, String downpay, String remaining, String setup_date, String send_date, String phar_type, String prog_price, String banks) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Update_cover_page_reactivate";
        final String SOAP_ACTION = "http://tempuri.org/Update_cover_page_reactivate";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("phar_code", phar_code);
        request.addProperty("rep_code", rep_code);
        request.addProperty("doc_name", doc_name);
        request.addProperty("created_by", created_by);
        request.addProperty("supervisor_name", supervisor_name);
        request.addProperty("snotes", snotes);
        request.addProperty("month_fee", month_fee);
        request.addProperty("cont_kind", cont_kind);
        request.addProperty("downpay", downpay);
        request.addProperty("remaining", remaining);
        request.addProperty("setup_date", setup_date);
        request.addProperty("send_date", send_date);
        request.addProperty("phar_type", phar_type);
        request.addProperty("prog_price", prog_price);
        request.addProperty("banks", banks);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //7
    public void Insert_another_fees(String phar_code, String disc, String rep_code, String amount, String reci_date,
                                    String reci_ser, String snotes, String pay_way, String cheque_ser, String cheque_date, String m_bank_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Insert_another_fees";
        final String SOAP_ACTION = "http://tempuri.org/Insert_another_fees";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("phar_code", phar_code);
        request.addProperty("rep_code", rep_code);
        request.addProperty("disc", disc);
        request.addProperty("amount", amount);
        request.addProperty("reci_date", reci_date);
        request.addProperty("reci_ser", reci_ser);
        request.addProperty("snotes", snotes);
        request.addProperty("pay_way", pay_way);
        request.addProperty("cheque_ser", cheque_ser);
        request.addProperty("cheque_date", cheque_date);
        request.addProperty("m_bank_code", m_bank_code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    ///////SR: Methods For new contract only//

    public void motaheda_code_vali(String motaheda_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "motaheda_code_vali";
        final String SOAP_ACTION = "http://tempuri.org/motaheda_code_vali";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("motaheda_code", motaheda_code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    public void Save_cover_page(String motaheda_code, String rep_code, String doc_name, String created_by,
                                String supervisor_name, String monthly_fee, String cont_kind, String down_pay, String remaining,
                                String setup_date, String date_created, String phar_type, String prog_price, String snotes
            , String tel1, String mob1, String phar_name, String address, String motaheda_branch, String b_disc) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Save_cover_page";
        final String SOAP_ACTION = "http://tempuri.org/Save_cover_page";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("motaheda_code", motaheda_code);
        request.addProperty("rep_code", rep_code);
        request.addProperty("doc_name", doc_name);
        request.addProperty("created_by", created_by);
        request.addProperty("supervisor_name", supervisor_name);
        request.addProperty("monthly_fee", monthly_fee);
        request.addProperty("cont_kind", cont_kind);
        request.addProperty("down_pay", down_pay);
        request.addProperty("remaining", remaining);
        request.addProperty("setup_date", setup_date);
        request.addProperty("date_created", date_created);
        request.addProperty("phar_type", phar_type);
        request.addProperty("prog_price", prog_price);
        request.addProperty("snotes", snotes);
        request.addProperty("tel1", tel1);
        request.addProperty("mob1", mob1);
        request.addProperty("address", address);
        request.addProperty("motaheda_branch", motaheda_branch);
        request.addProperty("phar_name", phar_name);
        request.addProperty("b_disc", b_disc);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceCoverData.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceCoverData.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceCoverData.ErrorID = 1;
        }


    }

    public void GetMotaheda_New_Code() {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "GetMotaheda_New_Code";
        final String SOAP_ACTION = "http://tempuri.org/GetMotaheda_New_Code";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceMotaheda_N_Code.motaheda_code = result.getProperty("motaheda_code").toString();
            WebServiceMotaheda_N_Code.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceMotaheda_N_Code.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    //SR: methods for order item for clinet  //

    //SR:01 method Get_invoice_number  == activate_pahr_code  //

    public void Get_invoice_number() {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Get_invoice_number";
        final String SOAP_ACTION = "http://tempuri.org/Get_invoice_number";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceInvoiceNumberData.invoice_number = result.getProperty("invoice_number").toString();
            WebServiceInvoiceNumberData.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceInvoiceNumberData.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    //SR:02 method save_phar_add_items    //

    //selled_count=الوحدات المباعة
    //price=قيمة الوحدة
    //add_no=ارقم الوحدة
    //total_price=قيمة الوحدة بعد ضربه القيمة مفرد في الوحدات المباعة
    //monthly_fee=قيمة الاشتراك
    //activate_pahr_code=كود يتم توليده من دالة 01

    public void save_phar_add_items(String selled_count, String price, String add_no, String total_price, String phar_code,
                                    String rep_code, String pay_notes, String monthly_fee, String activate_pahr_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "save_phar_add_items";
        final String SOAP_ACTION = "http://tempuri.org/save_phar_add_items";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("selled_count", selled_count);
        request.addProperty("price", price);
        request.addProperty("add_no", add_no);
        request.addProperty("total_price", total_price);
        request.addProperty("phar_code", phar_code);
        request.addProperty("rep_code", rep_code);
        request.addProperty("pay_notes", pay_notes);
        request.addProperty("monthly_fee", monthly_fee);
        request.addProperty("activate_pahr_code", activate_pahr_code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //SR:04 method Get_invoice_data  //

    public void Get_invoice_data(String phar_code, String rep_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Get_invoice_data";
        final String SOAP_ACTION = "http://tempuri.org/Get_invoice_data";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("rep_code", rep_code);
        request.addProperty("phar_code", phar_code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {


            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceUniData.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceUniData.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

            WebServiceUniData.uni_num = result.getProperty("uni_num").toString();
            WebServiceUniData.adds_total_price = result.getProperty("adds_total_price").toString();
            WebServiceUniData.invoice_no = result.getProperty("invoice_no").toString();


        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //SR:06 method for Update_uni_payed //

    public void Update_uni_payed(String phar_code, String invoice_no, String rep_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Update_uni_payed";
        final String SOAP_ACTION = "http://tempuri.org/Update_uni_payed";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("rep_code", rep_code);
        request.addProperty("phar_code", phar_code);
        request.addProperty("invoice_no", invoice_no);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {


            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());


        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //SR:07 method for save_pahr_another_fees_add //
    //SR: date yyyy-dd-MM//
    //SR: amount=total_price

    public void save_pahr_another_fees_add(String cl_code, String rep_code, String amount, String m_fee_date, String reciept_ser, String snotes,
                                           String pay_way, String cheque_ser, String cheque_date, String cashier_date, String invoice_no) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "save_pahr_another_fees_add";
        final String SOAP_ACTION = "http://tempuri.org/save_pahr_another_fees_add";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("rep_code", rep_code);
        request.addProperty("cl_code", cl_code);
        request.addProperty("amount", amount);
        request.addProperty("m_fee_date", m_fee_date);
        request.addProperty("reciept_ser", reciept_ser);
        request.addProperty("snotes", snotes);
        request.addProperty("pay_way", pay_way);
        request.addProperty("cheque_ser", cheque_ser);
        request.addProperty("cheque_date", cheque_date);
        request.addProperty("cashier_date", cashier_date);
        request.addProperty("invoice_no", invoice_no);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {


            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());


        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    //////////////////////////////////////////////////////////////


    //SR: methods for SETUP Client  //

    //SR:01 method Get_client_stage  //

    public void Get_client_stage(String code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Get_client_stage";
        final String SOAP_ACTION = "http://tempuri.org/Get_client_stage";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("code", code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {


            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());


        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 0;
        }


    }

    //SR:02 method for Update_client_stage//

    public void Update_client_stage(String phar_code, String stage_flag) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Update_client_stage";
        final String SOAP_ACTION = "http://tempuri.org/Update_client_stage";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("stage_flag", stage_flag);
        request.addProperty("phar_code", phar_code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {


            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());


        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //SR:02 method for Update_client_activation_flag//

    public void Update_client_activation_and_approve_sent_flag(String phar_code, String activation_flag,
                                                               String approve_flag, String sent_flag) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Update_client_activation_and_approve_sent_flag";
        final String SOAP_ACTION = "http://tempuri.org/Update_client_activation_and_approve_sent_flag";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("activation_flag", activation_flag);
        request.addProperty("approve_flag", approve_flag);
        request.addProperty("sent_flag", sent_flag);
        request.addProperty("phar_code", phar_code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {


            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());


        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    //SR:03 method for Update_client_setup_note //

    public void Update_client_setup_note(String phar_code, String note) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Update_client_setup_note";
        final String SOAP_ACTION = "http://tempuri.org/Update_client_setup_note";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("note", note);
        request.addProperty("phar_code", phar_code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {


            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());


        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }

    //SR:04 method for Get_visit_number //

    public void Get_visit_number(String cl_code, String rep_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Get_visit_number";
        final String SOAP_ACTION = "http://tempuri.org/Get_visit_number";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("cl_code", cl_code);
        request.addProperty("rep_code", rep_code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {


            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceVisitNumberData.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceVisitNumberData.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

            WebServiceVisitNumberData.visit_no = result.getProperty("visit_no").toString();


        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    //SR:05 method for Save_Visit_Client //

    public void Save_Visit_Client(String cl_code, String rep_code, String visit_no, String visit_date, String notes) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Save_Visit_Client";
        final String SOAP_ACTION = "http://tempuri.org/Save_Visit_Client";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("cl_code", cl_code);
        request.addProperty("rep_code", rep_code);
        request.addProperty("visit_no", visit_no);
        request.addProperty("visit_date", visit_date);
        request.addProperty("notes", notes);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {


            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());


        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    //SR:06 method for save_monthly_fees//
    //SR: date yyyy-dd-MM//
    public void save_monthly_fees(String pahr_code, String rep_code, String reciept_ser, String m_amount,
                                  String pay_way, String year, String month, String bank_code, String cashier_date, String pay_notes,
                                  String cheque_ser, String cheque_date, String visit_date) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "save_monthly_fees";
        final String SOAP_ACTION = "http://tempuri.org/save_monthly_fees";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("pahr_code", pahr_code);
        request.addProperty("rep_code", rep_code);
        request.addProperty("reciept_ser", reciept_ser);
        request.addProperty("m_amount", m_amount);
        request.addProperty("pay_way", pay_way);
        request.addProperty("year", year);
        request.addProperty("month", month);
        request.addProperty("bank_code", bank_code);
        request.addProperty("cashier_date", cashier_date);
        request.addProperty("pay_notes", pay_notes);
        request.addProperty("cheque_ser", cheque_ser);
        request.addProperty("cheque_date", cheque_date);
        request.addProperty("visit_date", visit_date);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {


            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());


        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    //SR:03 method for Monthly_Fee_vel//
    public void Monthly_Fee_vel(String month, String year, String cl_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";

        }


        final String METHOD_NAME = "Monthly_Fee_vel";
        final String SOAP_ACTION = "http://tempuri.org/Monthly_Fee_vel";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("month", month);
        request.addProperty("year", year);
        request.addProperty("cl_code", cl_code);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceMonthlyFeesResult.EX = Integer.parseInt(result.getProperty("EX").toString());
            WebServiceMonthlyFeesResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceMonthlyFeesResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceMonthlyFeesResult.ErrorID = 1;
        }


    }


    //////////////////////////////////////////////////////////////


    //SR: methods for Amounts to be paid //


    //SR:02 method for update fees for Amounts to Paid //
    //SR: date yyyy-dd-MM//
    public void update_fees_for_AP(String action, String tablename, String pay_way, String hewala_date, String hewala_ser, String bank_code, String ser, String reciept_serial) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";
        }


        final String METHOD_NAME = "update_fees_for_AP";
        final String SOAP_ACTION = "http://tempuri.org/update_fees_for_AP";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("action", action);
        request.addProperty("tablename", tablename);
        request.addProperty("pay_way", pay_way);
        request.addProperty("hewala_date", hewala_date);
        request.addProperty("hewala_ser", hewala_ser);
        request.addProperty("bank_code", bank_code);
        request.addProperty("ser", ser);
        request.addProperty("reciept_serial", reciept_serial);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WebServiceResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WebServiceResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
        }


    }


    //////////////////////////////////////////////////////////////


    //SR: methods for Notification ONLY//


    //SR:01 method Notification For unit is approve  //
    public void Noti_unit_app(String rep_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";
        }


        final String METHOD_NAME = "Noti_unit_app";
        final String SOAP_ACTION = "http://tempuri.org/Noti_unit_app";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("rep_code", rep_code);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WSNotificationResult.unit_app_count = result.getProperty("unit_app_count").toString();
            WSNotificationResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WSNotificationResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
            try {
                androidHttpTransport.getServiceConnection().disconnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    //SR:02 method Notification For unit is approve  //
    public void Noti_install(String rep_code) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";
        }


        final String METHOD_NAME = "Noti_install";
        final String SOAP_ACTION = "http://tempuri.org/Noti_install";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("rep_code", rep_code);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WSNotificationResult.install_count = result.getProperty("install_count").toString();
            WSNotificationResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WSNotificationResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
            try {
                androidHttpTransport.getServiceConnection().disconnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }


    //SR:03 method Notification For new in my Sall for EMP //
    public void Noti_nms(String rep_code, String status) {

        if (SPsr.getSP(context, "IP_iswork").length() > 0) {
            URL = "http://" + SPsr.getSP(context, "IP_iswork") + "/webser/android.asmx";
        }


        final String METHOD_NAME = "Noti_nms";
        final String SOAP_ACTION = "http://tempuri.org/Noti_nms";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("rep_code", rep_code);
        request.addProperty("status", status);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object response = null;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            WSNotificationResult.ms_count = result.getProperty("ms_count").toString();
            WSNotificationResult.ms_phar_name = result.getProperty("ms_phar_name").toString();
            WSNotificationResult.ms_phar_appr = result.getProperty("ms_phar_appr").toString();
            WSNotificationResult.ErrorMessage = result.getProperty("ErrorMessage").toString();
            WSNotificationResult.ErrorID = Integer.parseInt(result.getProperty("ErrorID").toString());

        } catch (Exception e) {
            e.printStackTrace();
            WebServiceResult.ErrorID = 1;
            try {
                androidHttpTransport.getServiceConnection().disconnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }


}
