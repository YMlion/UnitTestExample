package cn.futu.loginunittest.contact;

import java.util.List;

import cn.futu.loginunittest.data.Repository;
import cn.futu.loginunittest.data.model.Contact;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * contact presenter
 */
public class ContactPresenter implements ContactContract.Presenter
{
    private ContactContract.View mView;
    private Repository mRepository;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private String mId;

    public ContactPresenter(ContactContract.View view, Repository repository, String id)
    {
        mView = view;
        mRepository = repository;
        mId = id;
    }

    @Override
    public void destroy()
    {
        mView = null;
        mDisposable.clear();
    }

    @Override
    public void loadContactList()
    {
        mView.onLoadStart();
        mDisposable.add(mRepository.loadContactList(mId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Contact>>()
                {
                    @Override
                    public void accept(List<Contact> contacts)
                    {
                        if (contacts != null && !contacts.isEmpty())
                        {
                            mView.showContactList(contacts);
                        }
                        else
                        {
                            mView.showEmpty();
                        }
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable)
                    {
                        mView.onLoadFailed(throwable.getMessage());
                    }
                }));
    }
}
