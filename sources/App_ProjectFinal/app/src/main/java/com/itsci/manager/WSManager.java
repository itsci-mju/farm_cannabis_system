package com.itsci.manager;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.itsci.it411_asynctask.R;
import com.itsci.model.CustomerModel;
import com.itsci.model.CustomerModel2;
import com.itsci.model.HarvestModel;
import com.itsci.model.HarvestModel2;
import com.itsci.model.OrderDetailModel;
import com.itsci.model.OrderDetailModel2;
import com.itsci.model.OrderModel;
import com.itsci.model.OrderModel2;
import com.itsci.model.PaymentModel;
import com.itsci.model.PaymentModel2;
import com.itsci.model.PlantingModel;
import com.itsci.model.PlantingProgressModel;
import com.itsci.model.PlantingProgressModel2;
import com.itsci.model.ProductModel;
import com.itsci.model.RequestRegisterModel;
import com.itsci.model.ResponseModel;
import com.itsci.model.UserModel;
import com.itsci.task.WSTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Type;
import java.util.List;

public class WSManager {
    private static WSManager wsManager;
    private Context context;

    public interface WSManagerListener {
        void onComplete(Object response);
        void onError(String err);
    }

    public WSManager(Context context) {
        this.context = context;
    }

    public static WSManager getWsManager(Context context) {
        if (wsManager == null)
            wsManager = new WSManager(context);
        return wsManager;
    }

    //User
    public void doLogin(Object object, final WSManagerListener listener) {
        if (!(object instanceof UserModel)) {
            return;
        }
        UserModel userModel = (UserModel) object;
        userModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.e("response ", response);
                String jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (String) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onComplete(jarr);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", userModel.toJSONString());
        task.execute(context.getString(R.string.login_url), userModel.toJSONString());
    }

    public void insert_User(Object object, final WSManagerListener listener) {
        if (!(object instanceof UserModel)) {
            return;
        }
        UserModel userModel = (UserModel) object;
        userModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                UserModel userModel = new UserModel(response);
                listener.onComplete(userModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", userModel.toJSONString());
        task.execute(context.getString(R.string.insert_user), userModel.toJSONString());
    }

    public void getUser(Object object, final WSManagerListener listener) {
        if (!(object instanceof UserModel)) {
            return;
        }
        UserModel userModel = (UserModel) object;
        userModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONObject jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr =  json.getJSONObject("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                UserModel.User user = gson.fromJson(jarr.toString(), UserModel.User.class);

                listener.onComplete(user);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.get_user), userModel.toJSONString());
    }

    public void getUserType(String username, final WSManagerListener listener) {
        if (!(username instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.e("response ", response);
                String jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (String) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onComplete(jarr);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        Log.d("data ", username);
        task.execute(context.getString(R.string.get_usertype), username);
    }

    public void listUser(Object object, final WSManagerListener listener) {
        if (!(object instanceof UserModel)) {
             return;
         }
            WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                UserModel.User[] userArray = gson.fromJson(jarr.toString(), UserModel.User[].class);

                listener.onComplete(userArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_user), "");
    }

    //Product
    public void listProduct(Object object, final WSManagerListener listener) {
        if (!(object instanceof ProductModel)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                ProductModel.Product[] productArray = gson.fromJson(jarr.toString(), ProductModel.Product[].class);

                listener.onComplete(productArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_product), "");
    }

    //Order
    public void insert_Order(Object object, final WSManagerListener listener) {
        if (!(object instanceof OrderModel)) {
            return;
        }
        OrderModel orderModel = (OrderModel) object;
        orderModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                OrderModel orderModel = new OrderModel(response);
                listener.onComplete(orderModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        Log.e("insert order", orderModel.toJSONString());
        task.execute(context.getString(R.string.insert_order), orderModel.toJSONString());
    }

    public void listOrder(String customer, final WSManagerListener listener) {
        if (!(customer instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                OrderModel2.Order[] orderArray = gson.fromJson(jarr.toString(), OrderModel2.Order[].class);

                listener.onComplete(orderArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_order), customer);
    }

    public void listOrderByYear(String year, final WSManagerListener listener) {
        if (!(year instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                OrderModel2.Order[] orderArray = gson.fromJson(jarr.toString(), OrderModel2.Order[].class);

                listener.onComplete(orderArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_order_by_year), year);
    }

    public void listAllOrder(Object object, final WSManagerListener listener) {
        if (!(object instanceof OrderModel)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                OrderModel2.Order[] orderArray = gson.fromJson(jarr.toString(), OrderModel2.Order[].class);

                listener.onComplete(orderArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_all_order), "");
    }

    public void confirmOrder(String orderid, final WSManagerListener listener) {
        if (!(orderid instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                OrderModel orderModel = new OrderModel(response);
                listener.onComplete(orderModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", orderid);
        task.execute(context.getString(R.string.confirm_order), orderid);
    }

    public void update_OrderStatus(Object object, final WSManagerListener listener) {
        if (!(object instanceof OrderModel)) {
            return;
        }
        OrderModel orderModel = (OrderModel) object;
        orderModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                OrderModel orderModel = new OrderModel(response);
                listener.onComplete(orderModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", orderModel.toJSONString());
        task.execute(context.getString(R.string.update_order_status), orderModel.toJSONString());
    }

    //Order Detail
    public void insert_OrderDetail(Object object, final WSManagerListener listener) {
        if (!(object instanceof OrderDetailModel)) {
            return;
        }
        OrderDetailModel orderDetailModel = (OrderDetailModel) object;
        orderDetailModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                OrderDetailModel orderDetailModel = new OrderDetailModel(response);
                listener.onComplete(orderDetailModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        Log.e("insert orderDetail", orderDetailModel.toJSONString());
        task.execute(context.getString(R.string.insert_orderdetail), orderDetailModel.toJSONString());
    }

    public void listOrderDetailByOrderID(String orderid, final WSManagerListener listener) {
        if (!(orderid instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                OrderDetailModel2.OrderDetail[] orderDetailArray = gson.fromJson(jarr.toString(), OrderDetailModel2.OrderDetail[].class);

                listener.onComplete(orderDetailArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_orderdetail_by_orderid), orderid);
    }

    public void listOrderDetailByYear(String year, final WSManagerListener listener) {
        if (!(year instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                OrderDetailModel2.OrderDetail[] orderDetailArray = gson.fromJson(jarr.toString(), OrderDetailModel2.OrderDetail[].class);

                listener.onComplete(orderDetailArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_orderdetail_by_year), year);
    }

    public void listAllOrderDetail(Object object, final WSManagerListener listener) {
        if (!(object instanceof OrderDetailModel)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                OrderDetailModel2.OrderDetail[] orderDetailArray = gson.fromJson(jarr.toString(), OrderDetailModel2.OrderDetail[].class);

                listener.onComplete(orderDetailArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_all_orderdetail), "");
    }

    //Customer
    public void insert_Customer(Object object, final WSManagerListener listener) {
        if (!(object instanceof CustomerModel)) {
            return;
        }
        CustomerModel customerModel = (CustomerModel) object;
        customerModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                CustomerModel customerModel = new CustomerModel(response);
                listener.onComplete(customerModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", customerModel.toJSONString());
        task.execute(context.getString(R.string.insert_customer), customerModel.toJSONString());
    }

    public void getCustomer(String username, final WSManagerListener listener) {
        if (!(username instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONObject jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr =  json.getJSONObject("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                CustomerModel2.Customer customer = gson.fromJson(jarr.toString(), CustomerModel2.Customer.class);

                listener.onComplete(customer);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.get_customer), username);
    }

    public void listCustomer(Object object, final WSManagerListener listener) {
        if (!(object instanceof CustomerModel2)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                CustomerModel2.Customer[] customerArray = gson.fromJson(jarr.toString(), CustomerModel2.Customer[].class);

                listener.onComplete(customerArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_customer), "");
    }

    public void getCustomerByID(String id, final WSManagerListener listener) {
        if (!(id instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONObject jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr =  json.getJSONObject("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                CustomerModel2.Customer customer = gson.fromJson(jarr.toString(), CustomerModel2.Customer.class);

                listener.onComplete(customer);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.get_customerByID), id);
    }

    //RequestRegister
    public void insert_Register(Object object, final WSManagerListener listener) {
        if (!(object instanceof RequestRegisterModel)) {
            return;
        }
        RequestRegisterModel reqregModel = (RequestRegisterModel) object;
        reqregModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                RequestRegisterModel reqregModel = new RequestRegisterModel(response);
                listener.onComplete(reqregModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", reqregModel.toJSONString());
        task.execute(context.getString(R.string.insert_requestregister), reqregModel.toJSONString());
    }

    public void listRequestRegister(Object object, final WSManagerListener listener) {
        if (!(object instanceof RequestRegisterModel)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                RequestRegisterModel.RequestRegister[] requestregisterArray = gson.fromJson(jarr.toString(), RequestRegisterModel.RequestRegister[].class);

                listener.onComplete(requestregisterArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_requestregister), "");
    }

    public void Confirm_Registration(String registerid, final WSManagerListener listener) {
        if (!(registerid instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                RequestRegisterModel reqregModel = new RequestRegisterModel(response);
                listener.onComplete(reqregModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.confirm_registration), registerid);
    }

    public void Deny_Registration(String registerid, final WSManagerListener listener) {
        if (!(registerid instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                RequestRegisterModel reqregModel = new RequestRegisterModel(response);
                listener.onComplete(reqregModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.deny_registration), registerid);
    }

    //Planting
    public void listPlanting(Object object, final WSManagerListener listener) {
        if (!(object instanceof PlantingModel)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                PlantingModel.Planting[] plantingArray = gson.fromJson(jarr.toString(), PlantingModel.Planting[].class);

                listener.onComplete(plantingArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_planting), "");
    }

    public void listPlantingByYear(String year, final WSManagerListener listener) {
        if (!(year instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                PlantingModel.Planting[] plantingArray = gson.fromJson(jarr.toString(), PlantingModel.Planting[].class);

                listener.onComplete(plantingArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_planting_by_year), year);
    }

    public void insert_planting(Object object, final WSManagerListener listener) {
        if (!(object instanceof PlantingModel)) {
            return;
        }
        PlantingModel plantingModel = (PlantingModel) object;
        plantingModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                PlantingModel plantingModel = new PlantingModel(response);
                listener.onComplete(plantingModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", plantingModel.toJSONString());
        task.execute(context.getString(R.string.insert_planting), plantingModel.toJSONString());
    }

    public void update_planting(Object object, final WSManagerListener listener) {
        if (!(object instanceof PlantingModel)) {
            return;
        }
        PlantingModel plantingModel = (PlantingModel) object;
        plantingModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                PlantingModel plantingModel = new PlantingModel(response);
                listener.onComplete(plantingModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", plantingModel.toJSONString());
        task.execute(context.getString(R.string.update_planting), plantingModel.toJSONString());
    }

    public void delete_planting(Object object, final WSManagerListener listener) {
        if (!(object instanceof PlantingModel)) {
            return;
        }
        PlantingModel plantingModel = (PlantingModel) object;
        plantingModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                PlantingModel plantingModel = new PlantingModel(response);
                listener.onComplete(plantingModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", plantingModel.toJSONString());
        task.execute(context.getString(R.string.delete_planting), plantingModel.toJSONString());
    }

    //Payment
    public void insert_Payment(Object object, final WSManagerListener listener) {
        if (!(object instanceof PaymentModel)) {
            return;
        }
        PaymentModel paymentModel = (PaymentModel) object;
        paymentModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                PaymentModel paymentModel = new PaymentModel(response);
                listener.onComplete(paymentModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", paymentModel.toJSONString());
        task.execute(context.getString(R.string.insert_payment), paymentModel.toJSONString());
    }

    public void listPayment(Object object, final WSManagerListener listener) {
        if (!(object instanceof PaymentModel2)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                PaymentModel2.Payment[] paymentArray = gson.fromJson(jarr.toString(), PaymentModel2.Payment[].class);

                listener.onComplete(paymentArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_payment), "");
    }

    public void getPaymentByOrderID(String orderid, final WSManagerListener listener) {
        if (!(orderid instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONObject jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    if(json.get("result") != null){
                        jarr = (JSONObject) json.get("result");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(jarr != null) {
                    Gson gson = new Gson();
                    PaymentModel2.Payment payment = gson.fromJson(jarr.toString(), PaymentModel2.Payment.class);

                    listener.onComplete(payment);
                }else{
                    listener.onComplete(null);
                }
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.get_payment_by_orderid), orderid);
    }

    //Harvest
    public void listAllHarvest(Object object, final WSManagerListener listener) {
        if (!(object instanceof HarvestModel)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    if(json.get("result") != null){
                        jarr = (JSONArray) json.get("result");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(jarr != null) {
                    Gson gson = new Gson();
                    HarvestModel2.Harvest[] harvestArray = gson.fromJson(jarr.toString(), HarvestModel2.Harvest[].class);

                    listener.onComplete(harvestArray);
                }else{
                    listener.onComplete("0");
                }
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_all_harvest), "");
    }

    public void listHarvest(String planting, final WSManagerListener listener) {
        if (!(planting instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                HarvestModel2.Harvest[] harvestArray = gson.fromJson(jarr.toString(), HarvestModel2.Harvest[].class);

                listener.onComplete(harvestArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_harvest), planting);
    }

    public void listHarvestByYear(String year, final WSManagerListener listener) {
        if (!(year instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                HarvestModel2.Harvest[] harvestArray = gson.fromJson(jarr.toString(), HarvestModel2.Harvest[].class);

                listener.onComplete(harvestArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_harvest_by_year), year);
    }

    public void listDetailHarvest(Object object, final WSManagerListener listener) {
        if (!(object instanceof HarvestModel)) {
            return;
        }
        HarvestModel harvestModel = (HarvestModel) object;
        harvestModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                HarvestModel2.Harvest[] harvestArray = gson.fromJson(jarr.toString(), HarvestModel2.Harvest[].class);

                listener.onComplete(harvestArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.listdetail_harvest), harvestModel.toJSONString());
    }

    public void listHarvestGroupByHarvestID(String plantid, final WSManagerListener listener) {
        if (!(plantid instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    if(json.get("result") != null){
                        jarr = (JSONArray) json.get("result");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(jarr != null) {
                    Gson gson = new Gson();
                    HarvestModel2.Harvest[] harvestArray = gson.fromJson(jarr.toString(), HarvestModel2.Harvest[].class);

                    listener.onComplete(harvestArray);
                }else{
                    listener.onComplete("0");
                }
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_harvest_group_by_harvestid), plantid);
    }

    public void insert_Harvest(Object object, final WSManagerListener listener) {
        if (!(object instanceof HarvestModel)) {
            return;
        }
        HarvestModel harvestModel = (HarvestModel) object;
        harvestModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                HarvestModel harvestModel = new HarvestModel(response);
                listener.onComplete(harvestModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        Log.e("insert harvest", harvestModel.toJSONString());
        task.execute(context.getString(R.string.insert_harvest), harvestModel.toJSONString());
    }

    public void update_harvest(Object object, final WSManagerListener listener) {
        if (!(object instanceof HarvestModel)) {
            return;
        }
        HarvestModel harvestModel = (HarvestModel) object;
        harvestModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                HarvestModel harvestModel = new HarvestModel(response);
                listener.onComplete(harvestModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", harvestModel.toJSONString());
        task.execute(context.getString(R.string.update_harvest), harvestModel.toJSONString());
    }

    public void delete_harvest(Object object, final WSManagerListener listener) {
        if (!(object instanceof HarvestModel)) {
            return;
        }
        HarvestModel harvestModel = (HarvestModel) object;
        harvestModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                HarvestModel harvestModel = new HarvestModel(response);
                listener.onComplete(harvestModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", harvestModel.toJSONString());
        task.execute(context.getString(R.string.delete_harvest), harvestModel.toJSONString());
    }

    public void delete_Detailharvest(Object object, final WSManagerListener listener) {
        if (!(object instanceof HarvestModel)) {
            return;
        }
        HarvestModel harvestModel = (HarvestModel) object;
        harvestModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                HarvestModel harvestModel = new HarvestModel(response);
                listener.onComplete(harvestModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", harvestModel.toJSONString());
        task.execute(context.getString(R.string.deletedetail_harvest), harvestModel.toJSONString());
    }

    //Planting Progress
    public void listAllPlantingProgress(Object object, final WSManagerListener listener) {
        if (!(object instanceof PlantingProgressModel)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                PlantingProgressModel2.PlantingProgress[] ppArray = gson.fromJson(jarr.toString(), PlantingProgressModel2.PlantingProgress[].class);

                listener.onComplete(ppArray);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_all_plantingprogress), "");
    }

    public void listPlantingProgress(String planting, final WSManagerListener listener) {
        if (!(planting instanceof String)) {
            return;
        }
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.d("search ", response);
                JSONArray jarr = null;
                try {
                    JSONObject json = new JSONObject(response);
                    jarr = (JSONArray) json.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                PlantingProgressModel2.PlantingProgress[] ppArray = gson.fromJson(jarr.toString(), PlantingProgressModel2.PlantingProgress[].class);

                listener.onComplete(ppArray);

            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        task.execute(context.getString(R.string.list_plantingprogress), planting);
    }

    public void insert_PlantingProgress(Object object, final WSManagerListener listener) {
        if (!(object instanceof PlantingProgressModel)) {
            return;
        }
        PlantingProgressModel plantingProgressModel = (PlantingProgressModel) object;
        plantingProgressModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                PlantingProgressModel harvestModel = new PlantingProgressModel(response);
                listener.onComplete(harvestModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        Log.e("insert plantingprogress", plantingProgressModel.toJSONString());
        task.execute(context.getString(R.string.insert_plantingprogress), plantingProgressModel.toJSONString());
    }

    public void update_PlantingProgress(Object object, final WSManagerListener listener) {
        if (!(object instanceof PlantingProgressModel)) {
            return;
        }
        PlantingProgressModel plantingProgressModel = (PlantingProgressModel) object;
        plantingProgressModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                PlantingProgressModel plantingProgressModel = new PlantingProgressModel(response);
                listener.onComplete(plantingProgressModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", plantingProgressModel.toJSONString());
        task.execute(context.getString(R.string.update_plantingprogress), plantingProgressModel.toJSONString());
    }

    public void delete_PlantingProgress(Object object, final WSManagerListener listener) {
        if (!(object instanceof PlantingProgressModel)) {
            return;
        }
        PlantingProgressModel plantingProgressModel = (PlantingProgressModel) object;
        plantingProgressModel.toJSONString();
        WSTask task = new WSTask(this.context,new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                PlantingProgressModel plantingProgressModel = new PlantingProgressModel(response);
                listener.onComplete(plantingProgressModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });

        Log.d("data ", plantingProgressModel.toJSONString());
        task.execute(context.getString(R.string.delete_plantingprogress), plantingProgressModel.toJSONString());
    }
}
