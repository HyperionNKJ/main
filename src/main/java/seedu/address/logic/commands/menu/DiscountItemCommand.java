package seedu.address.logic.commands.menu;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENDINGINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERCENT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ITEMS;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.DisplayItemListRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.menu.Item;
import seedu.address.model.menu.Price;

/**
 * Gives an existing item in the menu a discount.
 */
public class DiscountItemCommand extends Command {

    public static final String COMMAND_WORD = "discount-item";

    public static final String COMMAND_ALIAS = "dci";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Gives the item identified "
            + "by the index number used in the displayed item list a discount based on the percent. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer, starting index) "
            + "[" + PREFIX_ENDINGINDEX + "INDEX](must be larger than the starting index) "
            + PREFIX_PERCENT + "PERCENT "
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PERCENT + "20";

    public static final String MESSAGE_DISCOUNT_ITEM_SUCCESS = "Discounted %1$d items";
    public static final String MESSAGE_REVERT_ITEM_SUCCESS = "Reverted %1$d items";

    private Index index;
    private Index endingIndex;
    private final double percent;
    private final boolean isAll;

    /**
     * @param index of the item in the filtered item list to edit
     * @param percent the percent of the discount
     */
    public DiscountItemCommand(Index index, Index endingIndex, double percent, boolean isAll) {
        requireNonNull(index);
        requireNonNull(percent);

        this.index = index;
        this.endingIndex = endingIndex;
        this.percent = percent;
        this.isAll = isAll;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Item> lastShownList = model.getFilteredItemList();

        if (isAll) {
            index = Index.fromZeroBased(0);
            endingIndex = Index.fromOneBased(lastShownList.size());
        }

        if (endingIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }

        int numOfItems = endingIndex.getOneBased() - index.getZeroBased();

        for (int i = index.getZeroBased(); i < endingIndex.getOneBased(); i++) {
            discountItem(model, Index.fromZeroBased(i), percent);
        }

        model.updateFilteredItemList(PREDICATE_SHOW_ALL_ITEMS);
        model.commitAddressBook();
        EventsCenter.getInstance().post(new DisplayItemListRequestEvent());
        String message = MESSAGE_DISCOUNT_ITEM_SUCCESS;
        if (percent == 0) {
            message = MESSAGE_REVERT_ITEM_SUCCESS;
        }
        return new CommandResult(String.format(message, numOfItems));
    }

    /**
     * Update the itemToDiscount with the discountedItem in the model.
     * @param model
     * @param index
     * @param percent
     * @throws CommandException
     */
    public static void discountItem(Model model, Index index, double percent) throws CommandException {
        List<Item> lastShownList = model.getFilteredItemList();

        Item itemToDiscount = lastShownList.get(index.getZeroBased());
        Item discountedItem = createDiscountedItem(itemToDiscount, percent);

        model.updateItem(itemToDiscount, discountedItem);
    }

    /**
     * Creates and returns a {@code Item} with the details of {@code itemToDiscount}.
     */
    public static Item createDiscountedItem(Item itemToDiscount, double percent) {
        assert itemToDiscount != null;
        double originalValue = itemToDiscount.getPrice().getOriginalValue();
        Price updatedPrice = new Price(String.format("%.2f", originalValue));
        updatedPrice.setValue(percent);

        return new Item(itemToDiscount.getName(), updatedPrice, itemToDiscount.getRecipe(), itemToDiscount.getTags());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DiscountItemCommand // instanceof handles nulls
                    && index.equals(((DiscountItemCommand) other).index) // state check
                    && endingIndex.equals(((DiscountItemCommand) other).endingIndex) // state check
                    && isAll == ((DiscountItemCommand) other).isAll // state check
                    && percent == ((DiscountItemCommand) other).percent); // state check
    }
}
