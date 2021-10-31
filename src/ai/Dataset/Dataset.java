package ai.Dataset;

import java.util.ArrayList;

public class Dataset  {
    ArrayList<float[]>data = new ArrayList<float[]>();
    ArrayList<float[]>desired = new ArrayList<float[]>();
    public Dataset(ArrayList<float[]>data,ArrayList<float[]>desired){
        this.data=data;
        this.desired=desired;
    }
    public Dataset(){

    }
    public void addData(float[]inputs, float[]desiredOutputs){
        data.add(inputs);
        desired.add(desiredOutputs);
    }

    public ArrayList<float[]> getData() {
        return data;
    }

    public void setData(ArrayList<float[]> data) {
        this.data = data;
    }

    public ArrayList<float[]> getDesired() {
        return desired;
    }

    public void setDesired(ArrayList<float[]> desired) {
        this.desired = desired;
    }
}
