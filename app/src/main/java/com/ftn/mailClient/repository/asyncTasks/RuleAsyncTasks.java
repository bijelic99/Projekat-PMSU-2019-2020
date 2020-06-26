package com.ftn.mailClient.repository.asyncTasks;

import com.ftn.mailClient.dao.RuleDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Rule;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.retrofit.RuleApi;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import retrofit2.Response;

import java.io.IOException;

public class RuleAsyncTasks  {

    public static class AddNewRuleAsyncTask extends LocalDatabaseCallbackAsyncTask<Rule, Void, Boolean>{
        private Long accountId;

        public AddNewRuleAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface, Long accountId) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
            this.accountId = accountId;
        }

        @Override
        protected Boolean doInBackground(Rule... rules) {
            Rule rule = rules[0];
            RuleDao ruleDao = localDatabase.ruleDao();
            RuleApi ruleApi = RetrofitClient.getApi(RuleApi.class);

            try {
                Response<Rule> response = ruleApi.addRule(accountId, rule).execute();
                if(response.isSuccessful()){
                    rule = response.body();
                    ruleDao.insert(rule);
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }


}
